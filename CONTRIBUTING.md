# Contributing to Loneliness Diary
Thank you for your interest in contributing to *Loneliness Diary*.  
This project welcomes thoughtful, respectful, and well-tested contributions.  
We appreciate your effort in making the project better.

## Pull request guidelines
### Before submitting
Please make sure to:
- [ ] Your code compiles: `./gradlew assembleDebug`
- [ ] All checks pass: `./gradlew ktlintCheck detekt`
- [ ] You have manually tested the changes

> A PR template will guide you through the required fields when opening a pull request.

### Merge strategy
We encourage using **Git squash** to group all your work into a single, meaningful commit before merging.

Only the **final squashed commit** and the **branch names** are required to follow the [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) format.

> ℹ️ Intermediate commits during the pull request can be free-form, as long as the final squash is clean.

> ⚠️ This is important because the Github Actions pipeles uses the final squashed commit to generate changelogs and organize releases.

## Getting started
1. Fork the repository
2. Clone the fork locally
3. Open the project in Android Studio
4. Target the `dev` branch
5. Creates a new branch for your pull request, following the conventional commits

| Change type | Branch example |
| --- | --- |
| New feature | `feat/my-awesome-change` |
| Bug fix | `fix/null-pointer-login` |
| Refactor | `refactor/cleanup-settings-viewmodel` |
| Documentation | `docs/update-readme` |
| Chore | `chore/update-ci-pipeline` |

## Questions or proposal?
If you want to propose a feature or design decision, open a Github issue with the label `enhancement`.

Thank you 🙌.
<br />
Made with ❤️ by @emenjivar

