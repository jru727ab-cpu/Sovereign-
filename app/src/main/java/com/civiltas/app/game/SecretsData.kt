package com.civiltas.app.game

import com.civiltas.app.data.EarnSource
import com.civiltas.app.data.SecretCategory
import com.civiltas.app.data.SecretEntry

val ALL_SECRETS: List<SecretEntry> = listOf(
    SecretEntry(
        id = "signal_01",
        title = "The First Signal",
        category = SecretCategory.LORE,
        description = "A fragmented transmission intercepted before the Collapse. Its origin remains disputed, but the coordinates embedded within changed everything.",
        hint = "Something was broadcast before the end. Reach your first milestone to hear it.",
        effect = "Unlocks the Catastrophe Forecast Meter",
        earnSource = EarnSource.MILESTONE
    ),
    SecretEntry(
        id = "hex_lattice",
        title = "Hex Lattice Foundations",
        category = SecretCategory.SACRED_GEOMETRY,
        description = "Ancient architectural schematics encoded in hexagonal geometry. The Order discovered these ratios appear repeatedly in stable post-collapse structures.",
        hint = "Research the geometry of survival. Investigate structural anomalies.",
        effect = "+5% build efficiency for all structures",
        earnSource = EarnSource.RESEARCH
    ),
    SecretEntry(
        id = "corridor_map",
        title = "Safe Corridor Map Fragment",
        category = SecretCategory.SURVIVAL_INTEL,
        description = "A partial map of navigable routes through the Exclusion Zones, compiled by Order scouts at great personal risk.",
        hint = "Send an expedition into the eastern ruins. They may not all return.",
        effect = "Reduces expedition casualty risk by 15%",
        earnSource = EarnSource.EXPEDITION
    ),
    SecretEntry(
        id = "strata_reading",
        title = "Resource Strata Reading",
        category = SecretCategory.RESOURCE_INTEL,
        description = "Deep geological survey data mapping underground ore deposits. The Order's geomancers spent decades compiling this.",
        hint = "Hit your ore production milestone to access survey data.",
        effect = "+3% ore yield from all mining operations",
        earnSource = EarnSource.MILESTONE
    ),
    SecretEntry(
        id = "compass_cipher",
        title = "The Compass Rose Cipher",
        category = SecretCategory.LORE,
        description = "The founding document of the Order of the Compass, encoded as a navigational cipher. It describes their original purpose: to map the unmappable future.",
        hint = "Some knowledge must be purchased. The Order does not give away its origin freely.",
        effect = "Unlocks Order of the Compass history and origin lore",
        earnSource = EarnSource.PURCHASE
    ),
    SecretEntry(
        id = "ley_line",
        title = "Ley Line Survey",
        category = SecretCategory.SACRED_GEOMETRY,
        description = "A network of energetic pathways beneath the surface, mapped by Order researchers using a combination of dowsing and satellite data.",
        hint = "Research the invisible infrastructure beneath your feet.",
        effect = "Unlocks special grid-aligned layout bonuses",
        earnSource = EarnSource.RESEARCH
    ),
    SecretEntry(
        id = "signal_02",
        title = "The Second Signal",
        category = SecretCategory.LORE,
        description = "A response to the First Signal, broadcast from an unknown location three days before the Catastrophe. It contains a warning — and a pattern.",
        hint = "Continue your journey. The second signal follows the first.",
        effect = "Reveals catastrophe pattern intel — forecast accuracy +10%",
        earnSource = EarnSource.MILESTONE
    ),
    SecretEntry(
        id = "deep_vein",
        title = "Deep Vein Sonar",
        category = SecretCategory.RESOURCE_INTEL,
        description = "Experimental sonar technology adapted by Order engineers to detect rare ore veins at extreme depths. Dangerous to operate alone.",
        hint = "Dispatch an expedition with sonar equipment to the deep zones.",
        effect = "Enables rare ore detection — chance to find premium deposits",
        earnSource = EarnSource.EXPEDITION
    ),
    SecretEntry(
        id = "convergence_protocol",
        title = "Convergence Protocol",
        category = SecretCategory.SURVIVAL_INTEL,
        description = "Emergency relocation procedures developed by Order tacticians after studying three failed colony collapses. Brutal in its efficiency.",
        hint = "Purchase this classified operational document from Order archives.",
        effect = "Emergency colony relocation intel — reduces catastrophe losses",
        earnSource = EarnSource.PURCHASE
    ),
    SecretEntry(
        id = "sacred_proportion",
        title = "Sacred Proportion Blueprint",
        category = SecretCategory.SACRED_GEOMETRY,
        description = "Architectural designs based on the golden ratio and Fibonacci sequences. Order builders claim these structures have never collapsed.",
        hint = "Research ancient mathematical traditions applied to modern construction.",
        effect = "+8% structural integrity bonus using golden ratio proportions",
        earnSource = EarnSource.RESEARCH
    ),
    SecretEntry(
        id = "archivist_entry",
        title = "The Archivist's Last Entry",
        category = SecretCategory.LORE,
        description = "A personal diary entry written by the Order's chief archivist three hours before the Catastrophe. They knew. They documented everything.",
        hint = "The archivist's words are waiting at a critical milestone.",
        effect = "Unlocks pre-catastrophe historical record — narrative reward",
        earnSource = EarnSource.MILESTONE
    ),
    SecretEntry(
        id = "gnosis_01",
        title = "Gnosis Fragment I",
        category = SecretCategory.LORE,
        description = "The first fragment of the Order's esoteric knowledge tier. Contains encoded understanding of why civilizations collapse — and how to prevent it.",
        hint = "True gnosis cannot be earned. It must be chosen. Purchase this fragment.",
        effect = "Tier 1 esoteric knowledge — unlocks advanced forecast interpretations",
        earnSource = EarnSource.PURCHASE
    )
)
