# ClauseClear — AI Legal Document Simplifier

> Understand any legal document in plain English. No law degree required.

ClauseClear is a privacy-first native Android app that analyzes legal documents using AI and returns structured, plain-language summaries with risk classifications — so you know what you're signing before you sign it.

**Educational tool only. Not legal advice.**

---

## Screenshots

<img width="390" height="800" alt="01_Splash" src="https://github.com/user-attachments/assets/ab159504-8eab-4a49-b115-46473520f98d" />
<img width="390" height="844" alt="02_Home_Dashboard" src="https://github.com/user-attachments/assets/6dbc45da-3f97-4912-8ce9-366c8e58776a" />
<img width="390" height="844" alt="03_Upload_Scan" src="https://github.com/user-attachments/assets/982f2eb7-33aa-4898-ad1e-cfabeac42c6c" />
<img width="390" height="844" alt="04_Processing" src="https://github.com/user-attachments/assets/331b5f6b-c94a-45ab-888a-b767b7409234" />
<img width="390" height="844" alt="05_Document_Summary" src="https://github.com/user-attachments/assets/f833b2e9-8613-4f29-96ae-bb82957d5a17" />
<img width="390" height="844" alt="06_Clause_List" src="https://github.com/user-attachments/assets/8c59dded-c709-41dc-a278-8a9fe4733277" />
<img width="390" height="844" alt="07_Clause_Detail" src="https://github.com/user-attachments/assets/7eb197e1-8d5d-476a-b452-dc29709c42be" />
<img width="390" height="844" alt="08_Compare_Documents" src="https://github.com/user-attachments/assets/65d97603-ee4b-4ee1-9957-e6629a078bcf" />
<img width="390" height="844" alt="09_Side_by_Side" src="https://github.com/user-attachments/assets/f8b46c94-408e-4038-9415-fc1c7a71d2ce" />
<img width="390" height="844" alt="10_Settings" src="https://github.com/user-attachments/assets/ea5843ec-37bc-4be4-849c-f1d40d7d12d8" />

---

## Features

- **Upload or scan** — PDF upload or camera scan via ML Kit OCR
- **AI clause analysis** — every clause extracted, classified, and explained
- **Risk classification** — HIGH / MEDIUM / INFO with color-coded badges
- **Plain English** — each clause explained in simple terms
- **Why it matters / What to watch for** — actionable context for every clause
- **Document comparison** — side-by-side diff of two versions (added, removed, modified)
- **Privacy-first** — no permanent server storage, no AI training on your documents
- **Local storage** — results saved to encrypted Room database on-device
- **Auto-delete** — optional 24-hour local data expiry

---

## Tech Stack

### Android
| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Async | Coroutines + Flow |
| Networking | Retrofit 2 + OkHttp 4 |
| Serialization | Kotlinx Serialization |
| Local DB | Room (SQLCipher optional) |
| Preferences | DataStore |
| PDF Parsing | PdfBox-Android |
| OCR | ML Kit Text Recognition v2 |
| Camera | CameraX |
| Navigation | Navigation Compose |
| Min SDK | API 24 (Android 7.0) |

### Backend
| Layer | Technology |
|---|---|
| API Gateway | Spring Boot 3.x (Java 17) |
| AI Service | Python FastAPI |
| AI Model | OpenAI GPT-4o |
| Database | PostgreSQL 15 |
| File Storage | AWS S3 (session-scoped) |
| Auth | JWT (RS256) |
| Deployment | Docker + Docker Compose |

---

## Project Structure

```
com.clauseclear/
├── ui/
│   ├── disclaimer/
│   ├── home/
│   ├── upload/
│   ├── processing/
│   ├── summary/
│   ├── clauselist/
│   ├── clausedetail/
│   ├── compare/
│   ├── sidebyside/
│   ├── settings/
│   └── components/
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
├── data/
│   ├── local/
│   │   ├── database/
│   │   └── datastore/
│   ├── remote/
│   │   ├── api/
│   │   ├── dto/
│   │   └── interceptor/
│   └── repository/
├── di/
├── navigation/
└── utils/
```

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog or newer
- JDK 17+
- Docker (for backend)
- OpenAI API key

### 1. Clone the repository

```bash
git clone https://github.com/your-username/clauseclear-android.git
cd clauseclear-android
```

### 2. Configure local properties

Create a `local.properties` file in the project root:

```properties
API_BASE_URL=https://api.clauseclear.com/
```

Or for local development:

```properties
API_BASE_URL=http://10.0.2.2:8080/
```

### 3. Run the backend

```bash
cd backend
cp .env.example .env
# Add your OPENAI_API_KEY to .env

docker-compose up -d
```

### 4. Build and run the Android app

Open the project in Android Studio and run on an emulator or device (API 24+).

---

## API Reference

### POST `/api/v1/analyze`

Analyze a legal document.

**Request**
```json
{
  "document_text": "Full extracted text of the document..."
}
```

**Response**
```json
{
  "summary": {
    "document_type": "Residential Lease Agreement",
    "duration": "12 months",
    "key_amounts": ["$2,500/month", "$5,000 security deposit"],
    "parties": ["ABC Realty", "John Doe"],
    "effective_date": "2024-01-01"
  },
  "risk_overview": {
    "high": 3,
    "medium": 7,
    "info": 12
  },
  "clauses": [
    {
      "id": "clause_001",
      "title": "Automatic Renewal",
      "risk": "HIGH",
      "original_text": "This agreement shall automatically renew...",
      "plain_english": "Your lease renews itself unless you cancel 60 days early.",
      "why_matters": "You could be locked into another year unexpectedly.",
      "watch_for": "Mark your calendar 60 days before lease ends."
    }
  ]
}
```

### POST `/api/v1/compare`

Compare two versions of a document.

**Request**
```json
{
  "old_text": "Original document text...",
  "new_text": "Updated document text..."
}
```

**Response**
```json
{
  "changes": [
    {
      "type": "MODIFIED_NUMERIC",
      "clause_title": "Late Payment Fee",
      "old_value": "$50",
      "new_value": "$150",
      "description": "Late fee increased by $100."
    }
  ]
}
```

---

## Security

- HTTPS only — cleartext traffic disabled via `network_security_config.xml`
- Certificate pinning via OkHttp `CertificatePinner`
- Raw document text never written to disk
- No document retention after AI processing completes
- No AI training on user documents (OpenAI opt-out header set)
- HttpLoggingInterceptor disabled in release builds
- GDPR-style privacy controls in Settings

---

## Legal Safety

This app is designed as an **educational summary tool only**.

- Never describes documents as illegal
- Never advises users to sign or not sign
- Disclaimer shown on every analysis session
- All AI output prefaced as informational summaries

---

## Roadmap

- [x] PDF upload + AI analysis
- [x] Clause breakdown with risk levels
- [x] Plain English explanations
- [x] Local storage (Room)
- [x] Settings with auto-delete
- [ ] Camera scan (ML Kit OCR)
- [ ] Document comparison (v2)
- [ ] Multi-language support
- [ ] Shareable summary PDF export
- [ ] Offline AI (on-device model)

---

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you'd like to change.

1. Fork the repo
2. Create your feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

Please follow the existing code style (MVVM + Clean Architecture, Kotlin idioms).

---

## License

```
MIT License

Copyright (c) 2024 ClauseClear

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
```

---

## Disclaimer

ClauseClear is an educational tool that provides AI-generated summaries of legal documents. It does not provide legal advice. Always consult a qualified legal professional before making decisions based on any legal document.

---

<p align="center">Built with Kotlin · Jetpack Compose · FastAPI · OpenAI</p>
