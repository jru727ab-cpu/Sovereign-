# MVP Scope — CIVILTAS Android

## Version: 0.1.0-mvp

### In Scope

| Feature | Status |
|---------|--------|
| Idle ore accumulation (OPS tick every 1s) | ✅ |
| Manual collect button (+1 OPS bonus) | ✅ |
| Upgrade drill (exponential cost, OPS boost) | ✅ |
| Offline progress (capped at 8 hours) | ✅ |
| Offline gain popup on resume | ✅ |
| SharedPreferences persistence | ✅ |
| 12 Secrets (Order of the Compass) | ✅ |
| Secrets Library UI with category filter | ✅ |
| Catastrophe Forecast Meter | ✅ |
| Bottom Navigation (3 tabs) | ✅ |
| Store stubs (non-functional) | ✅ |
| Dark theme (DeepNavy/Gold palette) | ✅ |
| Unit tests for IdleEngine | ✅ |

### Out of Scope (Post-MVP)

- Real IAP integration (Google Play Billing)
- Expeditions (active gameplay loop)
- Research system
- Ads (AdMob)
- Push notifications for offline completion
- Cloud save / account system
- Social features
- Seasonal catastrophe events
- Room database migration
- Analytics

### Guardrails

- **No Hilt** — Keep DI simple; manual factory pattern only
- **No Room** — SharedPreferences + org.json is sufficient for MVP data volume
- **No external image loading** — Emoji + Material icons only
- **No network calls** — Fully offline MVP
- **Offline cap: 8 hours** — Prevents exploit of setting clock forward; balances monetization
- **Unit tests required** for all pure logic in `game/` package
