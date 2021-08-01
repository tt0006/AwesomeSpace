# Awesome space

One of my first Android projects, where I learned how to create simple UI, basic network operations, simple json response parsing and basic usage of LiveData and ViewModel.

### Main panel

The app shows image/animation/video data, description and date picker icon.

<table>
  <tr>
    <td><image src="screenshots/main_panel.jpg" /></td>
    <td><image src="screenshots/main_panel2.jpg" /></td>
  </tr>
</table>

### Full screen image

You can check image in full screen and zoom it to see more details by taping on it.

<table>
  <tr>
    <td><image src="screenshots/full_screen.jpg" /></td>
    <td><image src="screenshots/zoom.jpg" /></td>
  </tr>
</table>

### View image for other date

You can check images for any previous date by selecting it in date picker.

<table>
  <tr>
    <td><image src="screenshots/date_picker.jpg" /></td>
    <td><image src="screenshots/other_date.jpg" /></td>
  </tr>
</table>

### Installation

The app is not available in Play Store at the moment. Tor run it download source code and open it in Android Studio, replace 'DEMO_KEY' with your api key (optiona) and run the app. You can run the app with 'DEMO_KEY' as well but it will be limited to 30 requests per IP address per hour or 50 requests per IP address per day. More details: https://api.nasa.gov/

#### Additional notes

Uses com.github.chrisbanes.photoview for zoom functionality.
Uses public API https://apod.nasa.gov/apod/astropix.html to show Astronomy picture of the day in app.
