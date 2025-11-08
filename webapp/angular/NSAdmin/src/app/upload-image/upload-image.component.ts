import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import Swal from 'sweetalert2/dist/sweetalert2.esm.js';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
// import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-upload-image',
  templateUrl: './upload-image.component.html',
  styleUrls: ['./upload-image.component.scss']
})
export class UploadImageComponent implements OnInit {

  constructor(private httpClient: HttpClient) { }
  title = 'ImageUploadApp';
  public selectedFile: any;
  public event1: any;
  imgURL: any;
  receivedImageData: any;
  base64Data: any;
  convertedImage: any;
  imageId!: number;
  imageModuleName!: string;
  dispalyPreview!: boolean;
  // validation error for file input
  fileError: string | null = null;

   imageURL = environment.IMAGES_API_URL+'upload';

  @Input()
  receivedParentMessage!: string;
  // Emit the uploaded image metadata (id, pic, etc.) so parent can use base64 immediately
  @Output() messageEvent = new EventEmitter<any>();
  @Output() fileSelected = new EventEmitter<boolean>();
  childMessage: string | undefined;

  ngOnInit() {
    this.dispalyPreview = false;
  }

  public onFileChanged(event: any) {
    this.imageModuleName = this.receivedParentMessage;
    // event may be a DOM Event; guard for target/files
    const files = event?.target?.files || (event?.files ? event.files : null);
    this.selectedFile = files ? files[0] : null;
    // notify parent whether a file is selected
    this.fileSelected.emit(!!this.selectedFile);
    // clear any previous validation error when user selects a file
    if (this.selectedFile) {
      this.fileError = null;
    }
    // Below part is used to display the selected image
    if (this.selectedFile) {
      const reader = new FileReader();
      reader.readAsDataURL(this.selectedFile);
      reader.onload = (event2) => {
        this.imgURL = reader.result;
      };
    }
  }


  // This part is for uploading
  onUpload() {
    // validate file selected
    if (!this.selectedFile) {
      this.fileError = 'Please choose a file before uploading.';
      this.dispalyPreview = false;
      return;
    }
    this.fileError = null;
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
          // Emit full received image data so parent can set preview (pic) and id
          this.messageEvent.emit(this.receivedImageData);
          // uploading completed -> indicate no pending file selection
          this.fileSelected.emit(false);
        },
        err => {
          console.error('Error Occured during saving:', err);
          // Prefer explicit server message when available
          let msg = 'Upload failed. Please try again.';
          try {
            if (err && err.status === 413) {
              msg = 'Maximum upload size exceeded.';
            } else if (err && err.error) {
              if (typeof err.error === 'string') {
                msg = err.error;
              } else if (err.error.message) {
                msg = err.error.message;
              } else if (err.error.error) {
                msg = err.error.error;
              } else {
                msg = JSON.stringify(err.error);
              }
            } else if (err && err.message) {
              msg = err.message;
            }
          } catch (e) {
            console.error('Error parsing upload error', e);
          }

          // Show a user-friendly alert with the specific server message when available
          Swal.fire({
            icon: 'error',
            title: 'Upload failed',
            text: msg,
            confirmButtonText: 'OK'
          });
        }
      );
  }
}
