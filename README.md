# Flood_Early_Warning_System

## Overview

The Flood Early Warning System is an Android application designed to provide real-time alerts and information regarding flood conditions. The application integrates with ThingSpeak to fetch data and presents it to users through an intuitive interface. The system aims to enhance community preparedness and safety by delivering timely warnings and safety information.

## Features

- **Real-time Flood Alerts**: Receive immediate notifications about flood conditions in your area.
- **Weather Updates**: Stay informed with the latest weather forecasts and updates.
- **Water Level Monitoring**: Access real-time data on water levels from various sensors.
- **Safety Guidelines**: Get safety tips and guidelines to follow during a flood emergency.
- **Emergency Contacts**: Quickly access emergency contact numbers and information.

## Hardware Components

- **HC-SR04 Ultrasonic Sensor**: Used to measure the distance to the water surface, providing data on water levels.
- **ESP8266 Wi-Fi Module**: Connects the sensor to the internet, allowing data transmission to ThingSpeak for remote monitoring and alerts.

## Project Structure

### MainActivity.java

The `MainActivity.java` file is the main entry point of the application. It sets up the navigation and initializes the fragments that make up the various screens of the app. It manages the overall lifecycle of the app and handles user interactions that affect the global state of the application.

### HomeFragment.java

The `HomeFragment.java` file defines the home screen of the application. This fragment displays an overview of the current flood risk status, including real-time data on water levels and any active flood alerts. It serves as the central hub where users can access other parts of the app.

### WeatherFragment.java

The `WeatherFragment.java` file is responsible for displaying detailed weather information and forecasts. It fetches weather data from an API and presents it to the user in an easily understandable format. This helps users stay informed about weather conditions that could influence flood risks.

### SafetyFragment.java

The `SafetyFragment.java` file provides essential safety tips and guidelines to follow during a flood. This fragment aims to educate users on best practices and precautions to take in the event of a flood, enhancing their preparedness and safety.

### EmergencyFragment.java

The `EmergencyFragment.java` file contains emergency contacts and important information for quick access during a flood. It ensures that users have all necessary contact information at their fingertips, which can be crucial in emergency situations.

### ThingSpeakClient.java

The `ThingSpeakClient.java` file handles communication with the ThingSpeak platform to fetch real-time data. ThingSpeak is used to gather data from various sensors placed in flood-prone areas. This client manages the API requests and parses the responses to provide up-to-date information on water levels and other relevant metrics.

### WaterLevelService.java

The `WaterLevelService.java` file manages the continuous monitoring and updating of water level data. It runs in the background to ensure that the app always has the latest information. This service is crucial for providing real-time alerts and keeping the user informed of any changes in flood conditions.

## How It Works

1. **Initialization**: When the app is launched, `MainActivity` initializes the navigation and sets up the fragments.
2. **Home Screen**: The `HomeFragment` displays an overview of the flood risk status. It fetches and updates real-time data from the `WaterLevelService`.
3. **Weather Updates**: Users can navigate to the `WeatherFragment` to get the latest weather forecasts, which are fetched from a weather API.
4. **Safety Tips**: The `SafetyFragment` provides users with guidelines and tips on how to stay safe during a flood.
5. **Emergency Contacts**: The `EmergencyFragment` lists important contact information that can be accessed quickly in case of an emergency.
6. **Data Fetching**: The `ThingSpeakClient` periodically fetches data from ThingSpeak. This data includes water levels and other metrics that indicate flood conditions.
7. **Background Service**: The `WaterLevelService` runs in the background, continuously monitoring water levels and updating the `HomeFragment` with the latest data. If certain thresholds are crossed, it can trigger alerts to notify the user of potential flood risks.

## Future Work

The Flood Early Warning System can be adapted for various other applications with slight modifications, such as:

- **Tank Water Level Monitoring**: Monitor and manage the water levels in tanks to prevent overflow or dry conditions.
- **Pond Water Management**: Keep track of water levels in ponds to maintain optimal conditions for aquatic life.
- **Watering Plants**: Automate the watering of plants based on soil moisture levels and weather conditions.

## Installation

To set up and run the Flood Early Warning System on your local machine, follow these steps:

1. **Clone the repository**:

   ```bash
   git clone https://github.com/your-username/flood-early-warning-system.git
   cd flood-early-warning-system
   ```

2. **Open the project in Android Studio**:

   - Download and install [Android Studio](https://developer.android.com/studio).
   - Open Android Studio and select "Open an existing Android Studio project".
   - Navigate to the cloned repository and select the project.

3. **Build the project**:

   - Let Android Studio download the necessary dependencies and build the project.

4. **Run the application**:
   - Connect an Android device or use an emulator.
   - Click on the "Run" button in Android Studio to deploy the application.

## Authors

- Mohammad Umar
- Sahil Shehzad
- Abdul Basit

## Contributing

We welcome contributions to the Flood Early Warning System! If you would like to contribute, please follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes and commit them (`git commit -m 'Add new feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Create a pull request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more information.

## Contact

For any questions or inquiries, please contact [your-email@example.com](mailto:your-email@example.com).

## Screenshots

Screenshots of the application can be found in the following Google Drive folder:
[Google Drive Screenshots](https://drive.google.com/drive/folders/1zTW0OVFtMBp16IZ_NFIiv2ZwMO584BpA?usp=sharing)
