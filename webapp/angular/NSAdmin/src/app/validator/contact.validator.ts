import { AbstractControl } from "@angular/forms";
import { map } from "rxjs/operators";

import { MyValidationService } from "src/app/service/myValidatorService";

export class ValidateContactNotTaken {
  static createValidator(service: MyValidationService) {
    return (control: AbstractControl) => {
      const value = control.value as string;
      return service
        .isContactTaken(value)
        .pipe(
          map((result: boolean) => (result ? { contactTaken: true } : null))
        );
    };
  }
}
