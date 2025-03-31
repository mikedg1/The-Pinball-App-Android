# The Pinball App

The Pinball App is an Android application that allows users to search the **Open Pinball Database (OPDB)** for pinball machine information. This is a greenfield Android development project created as a refresher after working on SDK development for a while.

---

## Features

- **Search Machines:** Find pinball machines through the Open Pinball Database (OPDB).
- **Change Logs:** Fetch and view the latest changes from the OPDB.
- **Details:** Access detailed information about specific machines.

---

## Getting Started

### Prerequisites

1. **OPDB API Key:** To use the OPDB API, you'll need to generate an API key from [OPDB](https://opdb.org/api).

### Setup Instructions

1. **Clone the Repository:** Clone this project to your local development environment.
1. **Configure the API Key:**
    - Open the `local.properties` file in the project directory.
    - Add your OPDB API token:
```
opdb.api.token=YOUR_API_KEY
```

Replace `YOUR_API_KEY` with your actual API key obtained from OPDB.

1**Build & Run the App:**
    - Open the project in Android Studio.
    - Sync the project with Gradle files.
    - Run the app on an emulator or physical device.

---

## Development

### Project Structure

- **Features:**
    - `Home`: The main screen with navigation across the app.
    - `Search`: Search machines using typeahead and other query parameters.
    - `ChangeLog`: View change logs fetched from OPDB.
    - `MachineDetail`: Display detailed information for a specific machine.

- **API Integration:**
    - The `OpdbApiService.kt` file contains the classes needed for interacting with the OPDB API. It uses:
        - **Retrofit:** For HTTP API calls.
        - **Moshi:** For JSON serialization and deserialization.

### Dependencies

- **Retrofit:** For API requests.
- **Moshi:** For parsing JSON data.
- **Jetpack Compose:** For building UI components.
- **Hilt (Dagger):** For dependency injection.
- **Navigation Compose:** For navigation within the app.

---

## API Usage

The app interacts directly with the OPDB API to fetch the necessary data:

- **Search Typeahead**
- **Full Search**
- **Machine Info**
- **Change Logs**

Ensure you have provided a valid API token to authenticate requests to OPDB.

---

## Notes for Developers:

- **Navigation:** The app uses Jetpack Compose Navigation and features a single `HomeScreen` entry point managing all routes: Search, Change Logs, Machine Info, and About sections.

---

## External Links

- [Open Pinball Database (OPDB) API Documentation](https://opdb.org/api)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [Moshi Documentation](https://github.com/square/moshi)

---

Feel free to contribute by submitting issues or pull requests! 🎉