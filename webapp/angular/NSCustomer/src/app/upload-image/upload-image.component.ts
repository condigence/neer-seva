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
  childMessage: any;
  imageModuleName: string;
  dispalyPreview: boolean;
  isFileSelected: boolean = false;
  isUploading: boolean = false;
  imageLoading: boolean = false;


  ngOnInit() {
    this.dispalyPreview = false;
    this.isFileSelected = false;
    this.isUploading = false;
    this.imageLoading = false;
  }

  @Input() receivedParentMessage: string;
  @Output() messageEvent = new EventEmitter<number>();

  public onFileChanged(event) {
    this.imageModuleName=this.receivedParentMessage;
    this.selectedFile = event.target.files[0];
    
    // Check if a file is selected
    if (this.selectedFile) {
      this.isFileSelected = true;
      // Below part is used to display the selected image
      let reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]);
      reader.onload = (event2) => {
        this.imgURL = reader.result;
      };
    } else {
      this.isFileSelected = false;
    }
  }


  // Remove selected image
  onRemoveImage() {
    this.selectedFile = null;
    this.imgURL = null;
    this.isFileSelected = false;
    this.convertedImage = null;
    this.dispalyPreview = false;
    this.isUploading = false;
    this.imageLoading = false;
    // Reset the file input
    const fileInput = document.querySelector('input[type="file"]') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
  }

  // This part is for uploading
  onUpload() {
    if (!this.isFileSelected) {
      return; // Safety check
    }
    
    this.isUploading = true;
    this.dispalyPreview = false;
    const uploadData = new FormData();
    uploadData.append('moduleName',  this.imageModuleName);
    uploadData.append('myFile', this.selectedFile, this.selectedFile.name);
    this.httpClient.post(environment.IMAGES_API_URL + 'upload', uploadData)
      .subscribe(
        res => {
          // console.log(res);
          this.receivedImageData = res;
          this.base64Data = this.receivedImageData.pic;
          this.convertedImage = 'data:image/jpeg;base64,' + this.base64Data;
        
          this.imageId = this.receivedImageData.id;
          // set childMessage so the template can display image id or any message
          this.childMessage = this.imageId;
          //Emit Data
          this.messageEvent.emit(this.imageId);
          // Reset file selection after successful upload
          this.isFileSelected = false;
          this.isUploading = false;
          this.imageLoading = true; // Show loader until image loads
          this.dispalyPreview = true;
        },
        err => {
          console.log('Error Occured duringng saving: ' + err);
          this.isUploading = false;
        }
      );
  }

  // Image load event handler
  onImageLoad() {
    this.imageLoading = false;
  }

  // Image error event handler
  onImageError() {
    this.imageLoading = false;
    console.error('Error loading preview image');
  }
}
