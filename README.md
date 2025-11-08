# ⏱ Smart Attendance Processing System (Pointage App)

![Banner](./screenshots/banner.png)
<!-- Replace with a screenshot or banner of your dashboard -->

![Angular](https://img.shields.io/badge/Angular-18-red?logo=angular)
![Node.js](https://img.shields.io/badge/Node.js-20-green?logo=node.js)
![Docker](https://img.shields.io/badge/Docker-blue?logo=docker)
![Kubernetes](https://img.shields.io/badge/Kubernetes-3776AB?logo=kubernetes)
![Jenkins](https://img.shields.io/badge/Jenkins-gray?logo=jenkins)
![ArgoCD](https://img.shields.io/badge/ArgoCD-orange?logo=argo)

---

## 🌟 Overview

The **Pointage App** is an intelligent attendance tracking system designed to automate HR workflows.  
Instead of manual Excel manipulation, HR simply **uploads a `.xlsx` file**, and the system:

- Automatically reads, cleans, and processes data  
- Sorts attendance by employee ID  
- Matches each **entry (input)** with its **corresponding exit (output)**  
- Calculates total working hours  
- Displays searchable and filterable employee reports  

This drastically reduces HR workload, improves accuracy, and provides real-time visibility of all employee attendance data.

---

## 🚀 Key Features

✅ Upload `.xlsx` attendance files directly — no folder structure needed  
✅ Automatic data parsing and sorting by employee ID  
✅ Smart pairing of input/output events  
✅ Duration and overtime calculations  
✅ Search by employee ID between two dates  
✅ View all employee attendance in one click  
✅ Clean and modern UI (Angular + Material)  
✅ Automated CI/CD using Jenkins and Docker  
✅ Kubernetes deployment via ArgoCD  

---

## 🧠 How It Works

### 1️⃣ Excel File Import  
HR uploads an `.xlsx` attendance file (not a folder).  
The backend instantly reads and structures the data.

### 2️⃣ Smart Algorithm  
The backend algorithm:
- Sorts data by **employee ID**
- Finds each **output** following its **input**
- Orders all events chronologically
- Calculates **total working time**

### 3️⃣ Report Visualization  
The frontend displays:
- Search by employee ID and date range  
- All employee attendance with durations  
- Total hours, late entries, and overtime summaries  

---

## 💼 Business Value

| Benefit        | Description |
|----------------|-------------|
| ⏳ **Time Saver** | Reduces HR workload from hours to seconds |
| ✅ **Accuracy** | Eliminates Excel formula and manual entry errors |
| 👩‍💼 **Transparency** | HR can track each employee’s attendance easily |
| 📊 **Automation** | Full automatic processing from Excel to dashboard |

---

## 📸 Screenshots

| Feature | Screenshot |
|----------|-------------|
| 🔍 Search by ID Between Two Dates | ![Search by ID](./screenshots/search-by-id.png) |
| 👥 View All Workers | ![All Workers](./screenshots/all-workers.png) |


---

## 🏗️ Architecture Overview

**Frontend:** Angular 18 + Angular Material  
**Backend:** Node.js (Express) + MySQL  
**CI/CD:** Jenkins + Docker  
**Orchestration:** Kubernetes (ArgoCD)  
**Environment:** VMware Virtual Machine  

---

## ⚙️ Deployment & CI/CD Pipeline

### 🔹 Jenkins Pipeline (Automated)
Each Git commit triggers Jenkins to:

1. Pull the latest code from GitHub  
2. Build Docker images (frontend + backend)  
3. Push images to a private registry  
4. Deploy automatically via ArgoCD to Kubernetes  

#### `Jenkinsfile`
```groovy
pipeline {
  agent any
  stages {
    stage('Build & Push Docker Images') {
      steps {
        sh 'docker-compose build'
        sh 'docker-compose push'
      }
    }
    stage('Deploy to K8s') {
      steps {
        sh 'kubectl apply -f k8s/'
      }
    }
  }
}



🔄 CI/CD Workflow Diagram


GitHub Commit
     ↓
 Jenkins Build & Test
     ↓
 Docker Image Build & Push
     ↓
   ArgoCD Sync
     ↓
 Kubernetes Deployment
     ↓
   Pointage App (Running)





