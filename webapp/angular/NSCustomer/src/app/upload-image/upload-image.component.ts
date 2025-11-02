import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';


@Component({
  selector: 'app-upload-image',
  templateUrl: './upload-image.component.html',
  styleUrls: ['./upload-image.component.scss']
})
export class UploadImageComponent implements OnInit {

  constructor(private httpClient: HttpClient) { }
  title = 'ImageUploadApp';
  public selectedFile;
  public event1;
  imgURL: any;
  receivedImageData: any;
  base64Data: any;
  convertedImage: any;
  imageId: number;
  childMessage: any;
  imageModuleName: string;
  dispalyPreview: boolean;

  ngOnInit() {
    this.dispalyPreview = false;
  }

  @Input() receivedParentMessage: string;
  @Output() messageEvent = new EventEmitter<number>();

  public onFileChanged(event) {
    this.imageModuleName=this.receivedParentMessage;
    this.selectedFile = event.target.files[0];
    // Below part is used to display the selected image
    let reader = new FileReader();
    reader.readAsDataURL(event.target.files[0]);
    reader.onload = (event2) => {
      this.imgURL = reader.result;
    };
  }


  // This part is for uploading
  onUpload() {
    this.dispalyPreview = true;
    const uploadData = new FormData();
    uploadData.append('moduleName',  this.imageModuleName);
    uploadData.append('myFile', this.selectedFile, this.selectedFile.name);
    this.httpClient.post('http://localhost:8090/image/upload', uploadData)
      .subscribe(
        res => {
          // console.log(res);
          this.receivedImageData = res;
          this.base64Data = this.receivedImageData.pic;
          this.convertedImage = 'data:image/jpeg;base64,' + this.base64Data;
          console.log(this.base64Data);
          console.log(this.convertedImage);
          this.imageId = this.receivedImageData.id;
          // set childMessage so the template can display image id or any message
          this.childMessage = this.imageId;
          //Emit Data
          this.messageEvent.emit(this.imageId);
        },
        err => console.log('Error Occured duringng saving: ' + err)
      );
  }
}
