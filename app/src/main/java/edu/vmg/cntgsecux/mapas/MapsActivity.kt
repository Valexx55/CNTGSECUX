package edu.vmg.cntgsecux.mapas

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import edu.vmg.cntgsecux.R
import edu.vmg.cntgsecux.databinding.ActivityMapsBinding
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var locationManager: LocationManager //SERVICIO PARA LA UBICACION (gps, RED, WIFI)

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient //PARA OBTENER LA UBICACIÓN DEL DISPOSITIVO
    private lateinit var locationRequest: LocationRequest//PETICIÓN
    private lateinit var locationCallback: LocationCallback//FUNCIÓN A LA VUELTA DE CUANDO CONSIGA LA UBICACIÓN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        this.locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        //PEDIR PERMISOS PELIGROSOS M 6
        //SI ESTAMOS EN VERSION SUPERIOR A LA 6, PEDIMOS PERMISO DE UBICACIÓN POR SER PELIGROSO
        //requestPermissions( , 757)
      // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 535)
       /* } else {

            if (gpsEstaActivado()) {
                //acceso a la ubicación
                accedoALaUbicacionGPS()
            } else {
                //solicitar Activivación del GPS
                solictarActivacionGPS()
            }
        }*/

    }

    private fun gpsEstaActivado(): Boolean {
        var gps_activo = false
        gps_activo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return gps_activo
    }

    private fun solictarActivacionGPS() {
        //lanzamos el menú de ajustes, para que solicite la activicación
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(intent, 77)
    }

    private fun accedoALaUbicacionGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create()//ojo LocationRequest de la librería extra
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(5000)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult != null) {
                    val location = locationResult.lastLocation
                    mostrarUbicacionObtenida(location)
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        }
    }

    private fun mostrarDirPostal(ubicacion: Location) {
        try {
            val geocoder = Geocoder(this, Locale("es"))
            val dirs = geocoder.getFromLocation(ubicacion.latitude, ubicacion.longitude, 1)
            //TODO mejora https://stackoverflow.com/a/74530087/4067559 implica control API superior
            if (dirs != null && dirs.size > 0) {
                val direccion = dirs[0]
                Log.d(
                    "MIAPP",
                    "Dirección = " + direccion.getAddressLine(0) + " CP " +
                            direccion.postalCode + " Localidad "
                            + direccion.locality
                )
            }
        } catch (e: Exception) {
            Log.e("MIAPP", "Error obteniendo la dirección postal", e)
        }
    }



    private fun mostrarUbicacionObtenida(ubicacion: Location) {
        Log.d("MIAPP", "Mostrando ubicación obtenida ... $ubicacion ")
        val ubicacion_actual = LatLng(ubicacion.latitude, ubicacion.longitude)
        mMap.addMarker(MarkerOptions().position(ubicacion_actual).title("Estoy aquí"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacion_actual))
        mostrarDirPostal(ubicacion)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //volvemos de ajustes y volvemos a comprobar si el GPS está activo
        if (gpsEstaActivado()) {
            //acceso a la ubicación
            accedoALaUbicacionGPS()
        } else {
            Log.d("MIAPP", "GPS DESACTIVADO")
            Toast.makeText(this, "GPS DESACTIVADO - SIN ACCESO A LA UBICACIÓN", Toast.LENGTH_SHORT)
                .show()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("MIAPP", "tenemos permiso para acceder a la ubicación")
            //?tengo acceso al GPS
            if (gpsEstaActivado()) {
                //acceso a la ubicación
                accedoALaUbicacionGPS()
            } else {
                //solicitar Activivación del GPS
                solictarActivacionGPS()
            }
        } else {
            Log.d("MIAPP", "NO tenemos permiso para acceder a la ubicación")
            Toast.makeText(this, "SIN ACCESO A LA UBICACIÓN", Toast.LENGTH_SHORT).show()
        }
    }
}