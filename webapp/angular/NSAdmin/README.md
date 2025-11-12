# Neerseva (NSAdmin)

An Angular admin webapp for the Neerseva project.

This README documents how to run the project locally, what I recently changed while improving responsiveness and UX, and a few developer notes.

## Quick start

1. Install dependencies

```powershell
npm install
```

2. Run the dev server

```powershell
npm start    # or: ng serve
```

Open http://localhost:4200/ in your browser.

3. Build for production

```powershell
npm run build    # ng build --configuration production
```

## Environment

- API endpoints and image upload URL are configured via the environment files in `src/environments/` (for example `environment.ts`).

## Recent work and important notes

I performed a set of responsive and UX fixes to stabilize the admin UI. Highlights:

- Fixed login and registration layouts and added a strict contact input validation (10 digits, numeric only).
- Made OTP and Sign Up buttons responsive and full-width on small screens.
- Reworked the My Profile page: responsive avatar, stacked action buttons on mobile, and a better file upload UI.
- Replaced the native file input with a hidden input + label button to remove the browser "No file chosen" text, kept Choose/Upload side-by-side, and show the selected filename.
- Added client-side validation: Upload will show an inline error if clicked with no file selected; Update will be disabled if a file is chosen but not uploaded.
- Added defensive CSS for SweetAlert / SweetAlert2 to ensure dialogs are centered and responsive across viewports.
- Removed legacy `sweetalert.min.js` assets to avoid conflicts with SweetAlert2 imports used in the codebase.
- Updated `.gitignore` to exclude common OS/editor/build artifacts.

## Known warnings (non-blocking)

- The production bundle currently exceeds the Angular CLI budget (initial total ~1.14 MB). I recommend running a bundle analysis to trim unused plugins or move large libraries to lazy-loaded modules.
- The build reports one skipped CSS rule (a malformed floating-label selector). I added an explicit, well-formed rule in `src/assets/css/app-style.css` to reproduce expected floating-label behavior; the warning persists but the UI is unaffected.

## Developer tips

- To untrack previously committed build files after updating `.gitignore` run:

```powershell
git rm -r --cached dist
git commit -m "Remove dist from repo and add to .gitignore"
```

- If you prefer reproducible installs across dev machines consider committing a lockfile (`package-lock.json` or `pnpm-lock.yaml`). The repo currently ignores lockfiles by default; adjust `.gitignore` if you want to track one.

## Next recommended tasks

- Run a bundle analyzer (source-map-explorer or webpack-bundle-analyzer) and remove/optimize the largest contributors.
- Fix the CSS optimizer step that produces the malformed selector (advanced): either patch the offending source or adjust the build's postcss/minifier config.
- Add unit and e2e tests for the upload flow and critical auth pages.

If you want, I can run the bundle analysis next and propose concrete reductions.
