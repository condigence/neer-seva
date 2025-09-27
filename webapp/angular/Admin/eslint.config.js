// @ts-check
/**
 * ESLint flat config replacement using the scoped TypeScript ESLint plugin
 * and angular-eslint plugin. This avoids the deprecated 'typescript-eslint'
 * package which caused a peer-dependency conflict with TypeScript 5.8.
 */
module.exports = {
  overrides: [
    {
      files: ["**/*.ts"],
      extends: [
        'eslint:recommended',
        'plugin:@typescript-eslint/recommended',
        'plugin:@typescript-eslint/recommended-requiring-type-checking',
        'plugin:@angular-eslint/recommended'
      ],
      parser: '@typescript-eslint/parser',
      parserOptions: {
        project: './tsconfig.json',
        sourceType: 'module'
      },
      plugins: ['@typescript-eslint', '@angular-eslint'],
      rules: {
        '@angular-eslint/directive-selector': [
          'error',
          {
            type: 'attribute',
            prefix: 'app',
            style: 'camelCase'
          }
        ],
        '@angular-eslint/component-selector': [
          'error',
          {
            type: 'element',
            prefix: 'app',
            style: 'kebab-case'
          }
        ],
        '@typescript-eslint/no-explicit-any': 'off',
        '@typescript-eslint/no-unused-vars': [
          'error',
          { argsIgnorePattern: '^_', varsIgnorePattern: '^_' }
        ],
        'no-undefined': 'off',
        'no-var': 'error',
        'prefer-const': 'error',
        'func-names': 'error',
        'id-length': 'error',
        'newline-before-return': 'error',
        'space-before-blocks': 'error',
        'no-alert': 'error'
      }
    },
    {
      files: ['**/*.html'],
      extends: [
        'plugin:@angular-eslint/template/recommended',
        'plugin:@angular-eslint/template/accessibility'
      ],
      rules: {}
    }
  ]
};
