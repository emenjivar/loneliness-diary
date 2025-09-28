# Engineering notes
This document records technical decisions and engineering insights through the development process.
It servers as a living history of trade-offs, experiments and rationale behind important decisions.

## Sep 21, 2025 - Inserting emotions into the entries
**Objective**:
Insert emotions into text and apply color coding based on emotion categories.
While this sounds straightforward, handling the insertions proved quite challenged due to the need
to maintain text editor integrity across numerous edge cases:

- **Index shifting on insertion**: Inserting an emotion mid-text requires shifting indices of all subsequent insertions
- **Partial deletion handling**: If any character of an inserted emotion is deleted, the entire emotion must be removed to prevent incomplete mood coloring
- **Index shifting on deletion**: Removing emotions mid-text requires negatively adjusting indices of subsequent insertions
- **Insertion collision prevention**: Block emotion insertion when cursor is positioned within an existing emotion's range

**UI Implementation**: Originally planed to use `/` that triggers a contextual menu for insertions,
but menu positioning became overly complex. Instead, drew inspiration of Notion's approach - Placed
insertion options at the bottom of the screen with `IME` padding to prevent keyboard occlusions.

## Jul 7, 2025 - Improving times for pull request pipeline
**Problem**: 
The pull request pipeline was configured to run `./gradlew assembleDebug` to validate that the app compiles.
However, due to the modular structure, the pipeline was taking around 8 minutes to complete in Github actions.

**Solution**: 
Replaced `assembleDebug` with `compileDebugSources` to verify the code compiles without generating the APK file.
This change reduced the execution time from `~8m` to approximately `1m45s`.
