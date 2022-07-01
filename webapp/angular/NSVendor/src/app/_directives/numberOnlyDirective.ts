import { Directive, ElementRef, HostListener, Input } from '@angular/core';


@Directive({
  selector: 'input[numbersOnly]'
})
export class NumberDirective {

  constructor(private element: ElementRef) { }
  @HostListener('input', ['$event']) onInputChange(event) {
    const initalValue = this.element.nativeElement.value;
    this.element.nativeElement.value = initalValue.replace(/[^0-9]*/g, '');
    if ( initalValue !== this.element.nativeElement.value) {
      event.stopPropagation();
    }
  }

}
