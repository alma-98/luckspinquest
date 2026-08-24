# LuckSpinQuest - Lucky Spin 3 Mode

## Modes

- PROBABILITY
- CONTROLLED
- HYBRID

## Required implementation

1. Add spin_mode to spin wheel/configuration.
2. Keep spin_rules for probability.
3. Validate total probability = 100%.
4. Publish immutable SpinRuleVersion.
5. Add SpinControlledResult entity/table.
6. Add AdminSpinControlController.
7. Add AdminSpinRuleController.
8. Update Spin Engine.
9. Keep POST /api/v1/spins/play.
10. Add audit logging for:
   - CHANGE_SPIN_MODE
   - CREATE_CONTROLLED_RESULT
   - UPDATE_CONTROLLED_RESULT
   - DELETE_CONTROLLED_RESULT
   - PUBLISH_RULE_VERSION

## Engine

PROBABILITY:
  active rule version
  -> active segments
  -> secure random
  -> weighted selection

CONTROLLED:
  active admin assignment
  -> exact segment
  -> result

HYBRID:
  active rule version
  -> admin constraints
  -> eligible segments
  -> weighted selection
  -> result

## OpenAPI

Generated:
docs/openapi/lucky-spin-admin-3-mode.yaml
