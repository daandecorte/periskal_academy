import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-content',
  imports: [RouterLink, TranslateModule],
  templateUrl: './content.component.html',
  styleUrl: './content.component.css'
})
export class ContentComponent {

  selectedLanguage: string="ENGLISH";
  videoUrl: string="";

  uploadVideo() {
    const fileInput = document.getElementById("videoInput") as HTMLInputElement;
    if (!fileInput || !fileInput.files ||fileInput.files.length === 0) {
      alert("Please select a video file.");
      return;
    }
    const formData = new FormData();
    formData.append("file", fileInput.files[0]);

    fetch("/api/upload", {
        method: "POST",
        body: formData
    })
    .then(response => response.text())
    .then(message => {
      this.videoUrl=message;
      alert("Video uploaded successfully");
      console.log(this.videoUrl);
    })
    .catch(error => alert("Upload failed: " + error));
  }
  changeVideoLanguage(language: string) {
    this.selectedLanguage=language
  }
}
