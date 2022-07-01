import {
  Component,
  ViewEncapsulation,
  ElementRef,
  Input,
  EventEmitter,
  OnInit,
  OnDestroy,
  Output,
} from "@angular/core";

import { ModalService } from "../service/modal.service";
@Component({
  selector: "jw-modal",
  templateUrl: "modal.component.html",
  styleUrls: ["modal.component.less"],
  encapsulation: ViewEncapsulation.None,
})
export class ModalComponent implements OnInit, OnDestroy {
  @Input() id: string;
  private element: any;

 // @Input() public user;
 // @Output() passEntry: EventEmitter<any> = new EventEmitter();


   order:any

  constructor(private modalService: ModalService, private el: ElementRef) {
    this.element = el.nativeElement;
  }

  ngOnInit(): void {
    // ensure id attribute exists
    if (!this.id) {
      console.error("modal must have an id");
      return;
    }
   

    // move element to bottom of page (just before </body>) so it can be displayed above everything else
    document.body.appendChild(this.element);

    // close modal on background click
    this.element.addEventListener("click", (el) => {
      if (el.target.className === "jw-modal") {
        this.close();
      }
    });

    // add self (this modal instance) to the modal service so it's accessible from controllers
    this.modalService.add(this);
  }

  // remove self from modal service when component is destroyed
  ngOnDestroy(): void {
    this.modalService.remove(this.id);
    this.element.remove();
  }

  // open modal
  open(order): void {
    this.element.style.display = "block";
    document.body.classList.add("jw-modal-open");
   this.order=order;
  //  console.log(order);

  }

  // close modal
  close(): void {
    this.element.style.display = "none";
    document.body.classList.remove("jw-modal-open");
  }

  // passBack() {
  //   this.passEntry.emit(this.user);
  //   this.element.close(this.user);
  // }
}
