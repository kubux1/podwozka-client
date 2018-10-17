package podwozka.podwozka.Maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public final class MapUtil {

    private static final float DEFAULT_ZOOM = 10;

    // Coordinates for Gdansk are used as a default location
    private static final LatLng GDANSK = new LatLng(54.3612063,18.5499454);

    public static void setUpDefaultCamera(Context context, GoogleMap mMap,
                                   LocationManager locationManager) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: ask for permission
            // @see https://developer.android.com/training/permissions/requesting
            mMap.moveCamera(CameraUpdateFactory.newLatLng(GDANSK));
        } else {
            Location location = locationManager.getLastKnownLocation(
                    LocationManager.NETWORK_PROVIDER);
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
        mMap.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
    }

    private MapUtil() {
    }
}
