# NewsSphere App
The News App allows users to view the latest top headlines and articles from various sources worldwide. It fetches real-time data from the NewsAPI, displaying the top stories on the home screen. Users can explore different news categories, view detailed articles, and bookmark their favorite content. The app provides an intuitive, responsive UI built with 
Jetpack Compose, following modern Android development practices.

The app is built using a MVVM (Model-View-ViewModel) design pattern. It leverages Kotlin, Coroutines, and Hilt dependency injection, and network requests. The local data is stored in Room Database, allowing users to persist saved articles.
# Features
- Fetch the latest headlines from *NewsAPI*
- Save articles for offline access
- View articles in a clean, user-friendly layout following Material Design principles
- Local caching with Room Database
- Network calls and image loading using Retrofit and Coil
# Steps to set up the project
- *Android Studio* with latest version
- *Kotlin* with latest version
- *Gradle* version 7.0+
- *API key* from *NewsApi*
## Clone the repository
You can clone the repository by using the url: *https://github.com/jatinacharya377/NewsSphere.git*. This can be done either from the Android Studio using *Import Project from Version Control* or you can run this git command "*git clone https://github.com/jatinacharya377/NewsSphere.git*".
## Set up the project
- *Import into Android Studio*: Open the Android Studio and import as an existing project
- *Add your API Key*: Open the *strings.xml* file located in "*res/values/*" and replace *API_KEY* with your valid API key if you want or you could continue with the API key provided.
- *Sync Gradle*: After adding the API key, sync the project with Gradle files to ensure all dependencies are resolved.
- *Run the Application*: Build and run the application on an emulator or a physical device.
# Libraries used and their purpose
- *Coil*: Image loading and caching library used for displaying images from URLs in an efficient manner.
- *ConstraintLayout*: For flexible, responsive, and efficient UI layouts.
- *Coroutines*: Handling asynchronous operations in a non-blocking manner.
- *Hilt*: Dependency Injection library for simplifying DI setup in Android apps.
- *Retrofit*: Retrofit is a type-safe HTTP client used to interact with REST APIs.
- *Room Database*: Local database solution for persistent storage.
# Explananation of the architecture and Design choices
## Architecture
The app follows a MVVM (Model-View-ViewModel) design pattern, which helps in separating concerns and ensures the code remains maintainable, testable, and scalable.
- *Model*: This consists of the data layer, including the data models, the network API interface (using Retrofit), and the Room Database for local storage.
- *View*: The UI layer is composed of Composables, which represent the UI elements and their states. The UI is declarative and responsive, following Material Design guidelines.
- *ViewModel*: The ViewModel acts as the intermediary between the UI and the data layers. It provides data to the view and manages UI-related data in a lifecycle-conscious way.
## Package Structure
The code is divided into the following packages to ensure modularity and maintainability:
- *data*: Contains submodules for the data layer, including models (model), remote network calls (remote), and local database (room).
- *ui*: Contains UI components, navigation logic, and screens.
- *viewmodel*: Contains the ViewModel classes for handling UI-related logic and providing data to the UI layer.
Additionally:
- *di*: Contains the Dependency Injection setup using Hilt for providing dependencies like the ViewModel and repository.
- *utils*: Contains utility functions like extension functions and helper methods.
# Design Choices
- *Jetpack Compose*: The app uses Jetpack Compose for building the UI. Compose offers a more concise and intuitive way to define UIs in Android and supports modern Android development practices.
- *Material Design*: The app follows Material Design principles to provide a consistent and visually appealing user experience. This includes components like cards, buttons, and text fields, as well as a bottom navigation bar for easy navigation between sections.
- *Bottom Navigation*: The home screen includes a Bottom Navigation Bar, allowing users to switch between different sections of the app (e.g., Home, Saved Articles).
- *Scaffold*: The app uses Scaffold as the layout container, which is the foundational layout component in Jetpack Compose. It provides an easy and standardized way to set up a basic structure for the app, including the top bar, bottom bar, floating action button (FAB), and the main content area.
- *Card Components*: Cards are used to display individual articles in a visually appealing manner, each with a title, image, and brief description. Cards are a common Material Design component that helps group content in a way that’s easy to read and interact with.
# Conclusion
The News App is designed to be a simple yet powerful news aggregator that gives users easy access to the latest headlines. With a MVVM architecture, modern UI built with Jetpack Compose, and a clean design, the app offers a smooth and efficient user experience. The app is built with maintainability and scalability in mind, making it easy to extend 
with additional features, such as article categorization or advanced offline functionality.
