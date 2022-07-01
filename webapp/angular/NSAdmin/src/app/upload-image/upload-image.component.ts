import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';


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
  imageModuleName: string;
  dispalyPreview: boolean;

   imageURL = environment.IMAGES_API_URL+'upload';

  @Input() receivedParentMessage: string;
  @Output() messageEvent = new EventEmitter<number>();

  ngOnInit() {
    this.dispalyPreview = false;
  }

  public onFileChanged(event) {
    this.imageModuleName = this.receivedParentMessage;
    this.selectedFile = event.target.files[0];
    // Below part is used to display the selected image
    const reader = new FileReader();
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
    //environment.BRANDS_API_URL
    this.httpClient.post(this.imageURL, uploadData)
      .subscribe(
        res => {
          // console.log(res);
          this.receivedImageData = res;
          this.base64Data = this.receivedImageData.pic;
          this.convertedImage = 'data:image/jpeg;base64,' + this.base64Data;
          console.log(this.base64Data);
          console.log(this.convertedImage);
          this.imageId = this.receivedImageData.id;
          // Emit Data
          this.messageEvent.emit(this.imageId);
        },
        err => console.log('Error Occured duringng saving: ' + err)
      );
  }
}
