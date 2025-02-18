import { Component, OnInit } from '@angular/core';
import { UploadService } from '../services/upload.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  selectedFile: File | null = null;

  constructor(private fileUploadService: UploadService) {}

  ngOnInit(): void {}

  onFileChange(event: any): void {
    this.selectedFile = event.target.files[0] || null;
    if (this.selectedFile) {
      console.log("Fichier sélectionné:", this.selectedFile.name);
    } else {
      console.warn("Aucun fichier sélectionné.");
    }
  }

  onSubmit(event: Event): void {
    event.preventDefault();
    console.log("onSubmit() called!");

    if (!this.selectedFile) {
      alert('Veuillez sélectionner un fichier avant de soumettre.');
      return;
    }

    console.log("Envoi du fichier:", this.selectedFile.name);

    this.fileUploadService.uploadFile(this.selectedFile).subscribe({
      next: (data: any) => {
        console.log("Réponse serveur:", data);
        alert("Fichier traité avec succès!");
      },
      error: (err: any) => {
        console.error("Erreur:", err);
        alert("Une erreur est survenue lors de l'envoi du fichier.");
      }
    });
  }
}
