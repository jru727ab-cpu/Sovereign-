package com.sovereign.wallet.utils

import java.security.MessageDigest
import java.security.SecureRandom
import kotlin.math.abs

/**
 * Wallet generation and restoration utilities.
 *
 * NOTE: This uses a simplified mnemonic/address scheme for offline-first MVP.
 * For production, replace with a proper BIP39/BIP44 library (e.g., web3j or bitcoinj).
 * The word list and derivation below are illustrative — swap in a certified implementation
 * before handling real assets.
 */
object WalletUtils {

    // BIP39-compatible 2048-word list (abbreviated for offline use — replace with full list in production)
    private val WORD_LIST = listOf(
        "abandon", "ability", "able", "about", "above", "absent", "absorb", "abstract",
        "absurd", "abuse", "access", "accident", "account", "accuse", "achieve", "acid",
        "acoustic", "acquire", "across", "act", "action", "actor", "actress", "actual",
        "adapt", "add", "addict", "address", "adjust", "admit", "adult", "advance",
        "advice", "aerobic", "afford", "afraid", "again", "age", "agent", "agree",
        "ahead", "aim", "air", "airport", "aisle", "alarm", "album", "alcohol",
        "alert", "alien", "all", "alley", "allow", "almost", "alone", "alpha",
        "already", "also", "alter", "always", "amateur", "amazing", "among", "amount",
        "amused", "analyst", "anchor", "ancient", "anger", "angle", "angry", "animal",
        "ankle", "announce", "annual", "another", "answer", "antenna", "anxiety", "any",
        "apart", "apple", "approve", "april", "arch", "arctic", "area", "arena",
        "argue", "armed", "armor", "army", "around", "arrange", "arrest", "arrive",
        "arrow", "art", "artefact", "artist", "artwork", "ask", "aspect", "assault",
        "asset", "assist", "assume", "asthma", "athlete", "atom", "attack", "attend",
        "attract", "auction", "audit", "august", "aunt", "author", "auto", "autumn",
        "average", "avocado", "avoid", "awake", "aware", "away", "awesome", "awful",
        "awkward", "axis", "baby", "balance", "bamboo", "banana", "banner", "bar",
        "barely", "bargain", "barrel", "base", "basic", "basket", "battle", "beach",
        "bean", "beauty", "become", "beef", "before", "begin", "behave", "behind",
        "believe", "below", "belt", "bench", "benefit", "best", "betray", "better",
        "between", "beyond", "bicycle", "bid", "bike", "bind", "biology", "bird",
        "birth", "bitter", "black", "blade", "blame", "blanket", "blast", "bleak",
        "bless", "blind", "blood", "blossom", "blouse", "blue", "blur", "blush",
        "board", "boat", "body", "boil", "bomb", "bone", "book", "boost",
        "border", "boring", "borrow", "boss", "bottom", "bounce", "box", "boy",
        "bracket", "brain", "brand", "brave", "breeze", "brick", "bridge", "brief",
        "bright", "bring", "brisk", "broccoli", "broken", "bronze", "broom", "brother",
        "brown", "brush", "bubble", "buddy", "budget", "buffalo", "build", "bulb",
        "bulk", "bullet", "bundle", "bunker", "burden", "burger", "burst", "bus",
        "business", "busy", "butter", "buyer", "buzz", "cabbage", "cabin", "cable",
        "cactus", "cage", "cake", "call", "calm", "camera", "camp", "cancel",
        "candy", "capable", "capital", "captain", "carbon", "card", "cargo", "carpet",
        "carry", "cart", "case", "cash", "casino", "castle", "casual", "catalog",
        "catch", "category", "cause", "cave", "ceiling", "celery", "cement", "census",
        "century", "cereal", "certain", "chair", "chalk", "champion", "change", "chaos",
        "chapter", "charge", "chase", "chat", "cheap", "check", "cheese", "chef",
        "cherry", "chess", "chicken", "chief", "child", "chimney", "choice", "choose",
        "chronic", "chuckle", "chunk", "cigar", "cinema", "circle", "citizen", "city",
        "civil", "claim", "clap", "clarify", "claw", "clay", "clean", "clerk",
        "clever", "click", "client", "cliff", "climb", "clinic", "clip", "clock",
        "clog", "close", "cloth", "cloud", "clown", "club", "clump", "cluster",
        "clutch", "coach", "coast", "coconut", "code", "coffee", "coil", "coin",
        "collect", "color", "column", "combine", "come", "comfort", "comic", "common",
        "company", "concert", "conduct", "confirm", "congress", "connect", "consider", "control",
        "convince", "cook", "cool", "copper", "copy", "coral", "core", "corn",
        "correct", "cost", "cotton", "couch", "country", "couple", "course", "cousin",
        "cover", "coyote", "crack", "cradle", "craft", "cram", "crane", "crash",
        "crazy", "cream", "credit", "creek", "crew", "cricket", "crime", "crisp",
        "critic", "cross", "crouch", "crowd", "crucial", "cruel", "cruise", "crumble",
        "crunch", "crush", "cry", "crystal", "cube", "culture", "cup", "cupboard",
        "curious", "current", "curtain", "curve", "cushion", "custom", "cute", "cycle",
        "dad", "damage", "damp", "dance", "danger", "daring", "dash", "daughter",
        "dawn", "day", "deal", "debate", "debris", "decade", "december", "decide",
        "decline", "decorate", "decrease", "deer", "defense", "define", "defy", "degree",
        "delay", "deliver", "demand", "demise", "denial", "dentist", "deny", "depart",
        "depend", "deposit", "depth", "deputy", "derive", "describe", "desert", "design",
        "desk", "despair", "destroy", "detail", "detect", "develop", "device", "devote",
        "diagram", "dial", "diamond", "diary", "dice", "diesel", "diet", "differ",
        "digital", "dignity", "dilemma", "dinner", "dinosaur", "direct", "dirt", "disagree",
        "discover", "disease", "dish", "dismiss", "disorder", "display", "distance", "divert",
        "divide", "divorce", "dizzy", "doctor", "document", "dog", "doll", "dolphin",
        "domain", "donate", "donkey", "donor", "door", "dose", "double", "dove",
        "draft", "dragon", "drama", "drastic", "draw", "dream", "dress", "drift",
        "drill", "drink", "drip", "drive", "drop", "drum", "dry", "duck",
        "dumb", "dune", "during", "dust", "dutch", "duty", "dwarf", "dynamic",
        "eager", "eagle", "early", "earn", "earth", "easily", "east", "easy",
        "echo", "edge", "educate", "effort", "egg", "eight", "either", "elbow",
        "elder", "electric", "elegant", "element", "elephant", "elite", "else", "embark",
        "embody", "embrace", "emerge", "emotion", "employ", "empower", "empty", "enable",
        "enact", "endless", "endorse", "enemy", "energy", "enforce", "engage", "engine",
        "enhance", "enjoy", "enlist", "enough", "enrich", "enroll", "ensure", "enter",
        "entire", "entry", "envelope", "episode", "equal", "equip", "erase", "erosion",
        "error", "erupt", "escape", "essay", "essence", "estate", "eternal", "ethics",
        "evidence", "evil", "evoke", "evolve", "exact", "example", "excess", "exchange",
        "excite", "exclude", "exercise", "exhaust", "exhibit", "exile", "exist", "exit",
        "exotic", "expand", "expire", "explain", "expose", "express", "extend", "extra",
        "eye", "fable", "face", "faculty", "faint", "faith", "fall", "false",
        "fame", "family", "famous", "fan", "fancy", "fantasy", "far", "fashion",
        "fat", "fatal", "father", "fatigue", "fault", "favorite", "feature", "february",
        "federal", "fee", "feed", "feel", "feet", "fellow", "fence", "festival",
        "fetch", "fever", "few", "fiber", "fiction", "field", "figure", "file",
        "film", "filter", "final", "find", "fine", "finger", "finish", "fire",
        "firm", "first", "fiscal", "fish", "fit", "fitness", "fix", "flag",
        "flame", "flash", "flat", "flavor", "flee", "flight", "flip", "float",
        "flock", "floor", "flower", "fluid", "flush", "fly", "foam", "focus",
        "fog", "foil", "follow", "food", "foot", "force", "forest", "forget",
        "fork", "fortune", "forum", "forward", "fossil", "foster", "found", "fox",
        "fragile", "frame", "frequent", "fresh", "friend", "fringe", "frog", "front",
        "frown", "frozen", "fruit", "fuel", "fun", "funny", "furnace", "fury",
        "future", "gadget", "gain", "galaxy", "gallery", "game", "gap", "garbage",
        "garden", "garlic", "garment", "gas", "gasp", "gate", "gather", "gauge",
        "gaze", "general", "genius", "genre", "gentle", "genuine", "gesture", "ghost",
        "giant", "gift", "giggle", "ginger", "giraffe", "girl", "give", "glad",
        "glance", "glare", "glass", "glide", "glimpse", "globe", "gloom", "glory",
        "glove", "glow", "glue", "goat", "goddess", "gold", "good", "goose",
        "gorilla", "gospel", "gossip", "govern", "gown", "grab", "grace", "grain",
        "grant", "grape", "grass", "gravity", "great", "green", "grid", "grief",
        "grit", "grocery", "group", "grow", "grunt", "guard", "guide", "guilt",
        "guitar", "gun", "gym", "habit", "hair", "half", "hammer", "hamster",
        "hand", "happy", "harsh", "harvest", "hat", "have", "hawk", "hazard",
        "head", "health", "heart", "heavy", "hedgehog", "height", "hello", "helmet",
        "help", "hero", "hidden", "high", "hill", "hint", "hip", "hire",
        "history", "hobby", "hockey", "hold", "hole", "hollow", "home", "honey",
        "hood", "hope", "horn", "horror", "hospital", "host", "hour", "hover",
        "hub", "huge", "human", "humble", "humor", "hundred", "hungry", "hunt",
        "hurdle", "hurry", "hurt", "husband", "hybrid", "ice", "icon", "idea",
        "identify", "idle", "ignore", "ill", "illegal", "image", "imitate", "immense",
        "immune", "impact", "impose", "improve", "impulse", "inbox", "income", "increase",
        "index", "indicate", "indoor", "industry", "infant", "inflict", "inform", "inhale",
        "inject", "inner", "innocent", "input", "inquiry", "insane", "insect", "inside",
        "inspire", "install", "intact", "interest", "into", "invest", "invite", "involve",
        "iron", "island", "isolate", "issue", "item", "ivory", "jacket", "jaguar",
        "jar", "jazz", "jealous", "jeans", "jelly", "jewel", "job", "join",
        "joke", "journey", "joy", "judge", "juice", "jump", "jungle", "junior",
        "junk", "just", "kangaroo", "keen", "keep", "ketchup", "key", "kick",
        "kid", "kingdom", "kiss", "kitchen", "kite", "kitten", "kiwi", "knee",
        "knife", "knock", "know", "lab", "label", "lamp", "language", "laptop",
        "large", "later", "laugh", "laundry", "lava", "law", "lawn", "lawsuit",
        "layer", "lazy", "leader", "learn", "leave", "lecture", "left", "leg",
        "legal", "legend", "leisure", "lemon", "lend", "length", "lens", "leopard",
        "lesson", "letter", "level", "liar", "liberty", "library", "license", "life",
        "lift", "like", "limb", "lion", "liquid", "list", "little", "live",
        "lizard", "load", "loan", "lobster", "local", "lock", "logic", "lonely",
        "long", "loop", "lottery", "loud", "lounge", "love", "loyal", "lucky",
        "luggage", "lunar", "lunch", "luxury", "mad", "magic", "magnet", "maid",
        "mail", "main", "major", "mansion", "manual", "maple", "marble", "march",
        "margin", "marine", "market", "marriage", "mask", "master", "match", "material",
        "math", "matrix", "matter", "maximum", "maze", "meadow", "measure", "medal",
        "media", "melody", "melt", "member", "memory", "mention", "mentor", "mercy",
        "merge", "merit", "mesh", "metal", "method", "middle", "midnight", "milk",
        "million", "mimic", "mind", "minimum", "minor", "minute", "miracle", "miss",
        "mix", "model", "modify", "moment", "monitor", "monkey", "monster", "month",
        "moon", "moral", "more", "morning", "mosquito", "mother", "motion", "motor",
        "mountain", "mouse", "move", "movie", "much", "muffin", "mule", "multiply",
        "muscle", "museum", "mushroom", "music", "must", "mutual", "myself", "mystery",
        "naive", "name", "napkin", "narrow", "nasty", "natural", "nature", "near",
        "neck", "need", "negative", "neglect", "neither", "nephew", "nerve", "nest",
        "network", "news", "next", "nice", "night", "noble", "noise", "nominee",
        "noodle", "normal", "north", "notable", "note", "nothing", "notice", "novel",
        "now", "nuclear", "number", "nurse", "nut", "oak", "obey", "object",
        "oblige", "obscure", "obtain", "ocean", "october", "odor", "off", "offer",
        "office", "often", "oil", "okay", "old", "olive", "olympic", "omit",
        "once", "onion", "open", "option", "orange", "orbit", "orchard", "order",
        "ordinary", "organ", "orient", "original", "orphan", "ostrich", "other", "outdoor",
        "output", "outside", "oval", "over", "own", "oyster", "ozone", "pact",
        "paddle", "page", "pair", "palace", "palm", "panda", "panel", "panic",
        "panther", "paper", "parade", "parent", "park", "parrot", "party", "pass",
        "patch", "path", "patrol", "pause", "pave", "payment", "peace", "peanut",
        "pear", "peasant", "pelican", "pen", "penalty", "pencil", "people", "pepper",
        "perfect", "permit", "person", "pet", "phone", "photo", "phrase", "physical",
        "piano", "picnic", "picture", "piece", "pig", "pigeon", "pill", "pilot",
        "pink", "pipe", "pistol", "pitch", "pizza", "place", "planet", "plastic",
        "plate", "play", "please", "pledge", "pluck", "plug", "plunge", "poem",
        "poet", "point", "polar", "pole", "police", "pond", "pony", "pool",
        "popular", "portion", "position", "possible", "post", "potato", "pottery", "poverty",
        "powder", "power", "practice", "praise", "predict", "prefer", "prepare", "present",
        "pretty", "prevent", "price", "pride", "primary", "print", "priority", "prison",
        "private", "prize", "problem", "process", "produce", "profit", "program", "project",
        "promote", "proof", "property", "prosper", "protect", "proud", "provide", "public",
        "pudding", "pull", "pulp", "pulse", "pumpkin", "punch", "pupil", "puppy",
        "purchase", "purity", "purpose", "push", "put", "puzzle", "pyramid", "quality",
        "quantum", "quarter", "question", "quick", "quit", "quiz", "quote", "rabbit",
        "raccoon", "race", "rack", "radar", "radio", "rage", "rail", "rain",
        "raise", "rally", "ramp", "ranch", "random", "range", "rapid", "rare",
        "rate", "rather", "raven", "reach", "ready", "real", "reason", "rebel",
        "rebuild", "recall", "receive", "recipe", "record", "recycle", "reduce", "reflect",
        "reform", "refuse", "region", "regret", "regular", "reject", "relax", "release",
        "relief", "rely", "remain", "remember", "remind", "remove", "render", "renew",
        "rent", "reopen", "repair", "repeat", "replace", "report", "require", "rescue",
        "resemble", "resist", "resource", "response", "result", "retire", "retreat", "return",
        "reunion", "reveal", "review", "reward", "rhythm", "ribbon", "ride", "ridge",
        "rifle", "right", "rigid", "ring", "riot", "ripple", "risk", "ritual",
        "rival", "river", "road", "roast", "robot", "robust", "rocket", "romance",
        "roof", "rookie", "rose", "rotate", "rough", "route", "royal", "rubber",
        "rude", "rug", "rule", "run", "runway", "rural", "sad", "saddle",
        "sadness", "safe", "sail", "salad", "salmon", "salon", "salt", "salute",
        "same", "sample", "sand", "satisfy", "satoshi", "sauce", "sausage", "save",
        "scale", "scan", "scatter", "scene", "scheme", "science", "scissors", "scorpion",
        "scout", "scrap", "screen", "script", "scrub", "sea", "search", "season",
        "seat", "second", "secret", "section", "security", "seek", "select", "sell",
        "seminar", "senior", "sense", "series", "service", "session", "settle", "setup",
        "seven", "shadow", "shaft", "shallow", "share", "shed", "shell", "sheriff",
        "shield", "shift", "shine", "ship", "shiver", "shock", "shoe", "shoot",
        "shop", "short", "shoulder", "shove", "shrimp", "shrug", "shuffle", "shy",
        "sibling", "siege", "sight", "silver", "similar", "simple", "since", "sing",
        "siren", "sister", "situate", "six", "size", "skate", "sketch", "ski",
        "skill", "skin", "skirt", "skull", "slab", "slam", "sleep", "slender",
        "slice", "slide", "slight", "slim", "slogan", "slot", "slow", "slush",
        "small", "smart", "smile", "smoke", "smooth", "snack", "snake", "snap",
        "sniff", "snow", "soap", "soccer", "social", "sock", "solar", "soldier",
        "solid", "solution", "solve", "someone", "song", "soon", "sorry", "soul",
        "sound", "soup", "source", "south", "space", "spare", "spatial", "spawn",
        "speak", "special", "speed", "sphere", "spice", "spider", "spike", "spin",
        "spirit", "split", "spoil", "sponsor", "spoon", "spray", "spread", "spring",
        "spy", "square", "squeeze", "squirrel", "stable", "stadium", "staff", "stage",
        "stairs", "stamp", "stand", "start", "state", "stay", "steak", "steel",
        "stem", "step", "stereo", "stick", "still", "sting", "stock", "stomach",
        "stone", "stop", "store", "stream", "street", "strike", "strong", "struggle",
        "student", "stuff", "stumble", "subject", "submit", "subway", "success", "such",
        "sudden", "suffer", "sugar", "suggest", "suit", "sunny", "sunset", "super",
        "supply", "supreme", "sure", "surface", "surge", "surprise", "sustain", "swallow",
        "swamp", "swap", "swear", "sweet", "swift", "swim", "swing", "switch",
        "sword", "symbol", "symptom", "syrup", "table", "tackle", "tag", "tail",
        "talent", "tank", "tape", "target", "task", "tattoo", "taxi", "teach",
        "team", "tell", "ten", "tenant", "tennis", "tent", "term", "test",
        "text", "thank", "that", "theme", "then", "theory", "there", "they",
        "thing", "this", "thought", "three", "thrive", "throw", "thumb", "thunder",
        "ticket", "tilt", "timber", "time", "tiny", "tip", "tired", "title",
        "toast", "tobacco", "today", "toddler", "token", "tomato", "tomorrow", "tone",
        "tongue", "tonight", "tool", "topic", "topple", "torch", "tornado", "tortoise",
        "toss", "total", "tourist", "toward", "tower", "town", "toy", "track",
        "trade", "traffic", "tragic", "train", "transfer", "trap", "trash", "travel",
        "tray", "treat", "tree", "trend", "trial", "trick", "trigger", "trim",
        "trip", "trophy", "trouble", "truck", "truly", "trumpet", "trust", "truth",
        "try", "tube", "tuition", "tumble", "tuna", "tunnel", "turkey", "turn",
        "turtle", "twelve", "twenty", "twice", "twin", "twist", "two", "type",
        "typical", "ugly", "umbrella", "unable", "uncle", "under", "unaware", "unique",
        "universe", "unknown", "until", "unusual", "unveil", "update", "upgrade", "uphold",
        "upon", "upper", "upset", "urban", "useful", "useless", "usual", "utility",
        "vacant", "vacuum", "vague", "valid", "valley", "valve", "van", "vanish",
        "vapor", "various", "vault", "vehicle", "velvet", "vendor", "venture", "venue",
        "verb", "verify", "version", "very", "veteran", "viable", "vibrant", "vicious",
        "victory", "video", "view", "village", "vintage", "violin", "virtual", "virus",
        "visa", "visit", "visual", "vital", "vivid", "vocal", "voice", "void",
        "volcano", "volume", "vote", "voyage", "wage", "wagon", "wait", "walk",
        "wall", "walnut", "want", "warfare", "warm", "warrior", "waste", "water",
        "wave", "way", "wealth", "weapon", "wear", "weasel", "web", "wedding",
        "weekend", "weird", "welcome", "well", "west", "wet", "whale", "wheat",
        "wheel", "when", "where", "whip", "whisper", "wide", "width", "wife",
        "wild", "will", "win", "window", "wine", "wing", "wink", "winner",
        "winter", "wire", "wisdom", "wish", "witness", "wolf", "woman", "wonder",
        "wood", "wool", "word", "world", "worry", "worth", "wrap", "wreck",
        "wrestle", "wrist", "write", "wrong", "yard", "year", "yellow", "you",
        "young", "youth", "zebra", "zero", "zone", "zoo"
    )

    private val secureRandom = SecureRandom()

    /**
     * Generate a 12-word mnemonic phrase using a cryptographically secure random source.
     * For production: integrate a full BIP39 library with checksum validation.
     */
    fun generateMnemonic(): String {
        val words = (1..12).map { WORD_LIST[abs(secureRandom.nextInt()) % WORD_LIST.size] }
        return words.joinToString(" ")
    }

    /**
     * Derive a deterministic wallet address from a mnemonic.
     * For production: use proper BIP44 HD derivation with secp256k1 or ed25519.
     */
    fun mnemonicToAddress(mnemonic: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(mnemonic.toByteArray(Charsets.UTF_8))
        return "0x" + hash.take(20).joinToString("") { "%02x".format(it) }
    }

    /**
     * Validate that a mnemonic phrase has the expected word count and all words are in the word list.
     */
    fun isValidMnemonic(mnemonic: String): Boolean {
        val words = mnemonic.trim().lowercase().split("\\s+".toRegex())
        if (words.size != 12 && words.size != 24) return false
        return words.all { it in WORD_LIST }
    }

    /**
     * Abbreviate a wallet address for display (e.g. 0x1a2b...f8e9)
     */
    fun abbreviateAddress(address: String): String {
        return if (address.length > 12) "${address.take(6)}...${address.takeLast(4)}" else address
    }
}
