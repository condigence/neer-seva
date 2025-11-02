# ShopProject (NSCustomer)

This repository contains the frontend web application for the NSCustomer project. It's an Angular application scaffolded with the Angular CLI and configured for local development and testing.

## Quick overview

- Framework: Angular (v18)
- Language: TypeScript
- App entry: `src/main.ts`
- App code: `src/app/`

## Prerequisites

- Node.js (recommended 18.x or later)
- npm (comes with Node) or yarn/pnpm if you prefer (this project uses npm by default)
- Angular CLI (optional globally): `npm i -g @angular/cli`

## Install

Open a terminal at the project root (`d:/gitrepo/neer-seva/webapp/angular/NSCustomer`) and run:

```powershell
npm install
```

This will install dependencies listed in `package.json`.

## Development server

Run the dev server with:

```powershell
npm start
# or
ng serve
```

Open http://localhost:4200/ in your browser. The app reloads on file changes.

## Available scripts

The `package.json` includes these npm scripts (aliases to Angular CLI commands):

- `npm start` — `ng serve` (run dev server)
- `npm run build` — `ng build` (build app to `dist/`)
- `npm test` — `ng test` (run unit tests via Karma)
- `npm run lint` — `ng lint` (run linter)
- `npm run e2e` — `ng e2e` (run e2e tests)

Example: create a production build:

```powershell
npm run build -- --configuration production
```

## Environments

Environment files are in `src/environments/` (`environment.ts`, `environment.prod.ts`). Add local overrides as needed but avoid committing secrets. If you need per-developer local settings, use a `.env` file and keep it out of source control.

## Testing

Unit tests run with Karma/Jasmine:

```powershell
npm test
```

End-to-end tests (Protractor) run with:

```powershell
npm run e2e
```

## Linting

Run ESLint/TS linting with:

```powershell
npm run lint
```

## Project structure (important folders)

- `src/app/` — main application code (components, services, modules)
- `src/assets/` — images, fonts, styles
- `src/environments/` — environment configs

## Contributing / Notes for developers

- Commit lockfiles only if your team wants deterministic installs. The default `.gitignore` in this repo currently ignores `package-lock.json` — remove it from `.gitignore` if you want to commit it.
- Keep shared configuration (lint, tsconfig) consistent across the team.
- If you add native modules or change Node targets, update `engines` in `package.json` and document any extra setup steps here.

## Troubleshooting

- If `npm install` fails, remove `node_modules` and retry: `rm -r node_modules; npm install` (PowerShell: `Remove-Item -Recurse node_modules; npm install`).
- If the dev server port 4200 is busy, run: `ng serve --port 4300` or change in the `start` script.

## License & Contact

Add your license and contact/maintainer info here (e.g., `MIT` or company policy).

---

If you'd like, I can also:

- add a short CONTRIBUTING.md
- add badges (build, coverage)
- add a developer quick-start script

Tell me which you prefer and I will add them.
