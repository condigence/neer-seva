// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  // Use relative path to enable the Angular dev-server proxy during development
  apiUrl: '/neerseva/api',
  // Full backend URLs (useful during development if you want to bypass the Angular proxy)
  AUTH_API_URL: 'http://localhost:9092/neerseva/api',
  USERS_API_URL: 'http://localhost:9092/neerseva/api/v1/users/',
  BRANDS_API_URL: 'http://localhost:9091/neerseva/api/v1/products/brands/',
  ITEMS_API_URL: 'http://localhost:9091/neerseva/api/v1/products/items/',
  IMAGES_API_URL: 'http://localhost:9090/neerseva/api/v1/images/'
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
