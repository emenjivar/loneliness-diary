# Engineering notes
This document records technical decisions and engineering insights throught the development process.
It servers as a living history of trafe-offs, experiments and rationale behing important decisions.

## Jul 7, 2025 - Improving times for pull request pipeline
**Problem**: 
The pull request pipeline was configured to run `./gradlew assembleDebug` to validate that the app compiles.
However, due to the modular structure, the pipeline was taking around 8 minutes to complete in Github actions.

**Solution**: 
Replaced `assembleDebug` with `compileDebugSources` to verify the code compiles without generating the APK file.
This change reduced the execution time from `~8m` to approximately `1m45s`.
