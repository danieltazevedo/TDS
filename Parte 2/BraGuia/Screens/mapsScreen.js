import React, { useEffect, useState } from 'react';
import { StyleSheet, View, Button } from 'react-native';
import MapView, { Circle, Marker, Polyline } from 'react-native-maps';
import * as Location from 'expo-location';
import axios from 'axios';

const MapsScreen = ({ route, navigation }) => {
  const [currentLocation, setCurrentLocation] = useState(null);
  const [currentLocationSet, setCurrentLocationSet] = useState(false);
  const [mapRegion, setMapRegion] = useState(null);
  const [routeCoordinates, setRouteCoordinates] = useState([]);
  const { item } = route.params;

  const handleTrails = () => {
    navigation.navigate('TrailsActivity');
  };

  const handleMarkerPress = (markerKey) => {
    let value = null;
  
    item.edges.map((edge) => {
      if (markerKey === edge.edge_start.pin_name) {
        value = edge.edge_start;
      }
    });
  
    if (markerKey === item.edges[item.edges.length - 1].edge_end.pin_name) {
      value = item.edges[item.edges.length - 1].edge_end;
    }
    navigation.navigate('MarkerInfo', { item: value });
  };
  

  const getDirections = async () => {
    try {
      const apiKey = 'AIzaSyCCC_fAp_0bloz8XP2Hd-9-f6dIvTF0KpU';
      const waypoints = item.edges
        .map((edge) => `${edge.edge_start.pin_lat},${edge.edge_start.pin_lng}`)
        .join('|');

      const response = await axios.get(
        `https://maps.googleapis.com/maps/api/directions/json?origin=${currentLocation.latitude},${currentLocation.longitude}&destination=${item.edges[item.edges.length - 1].edge_end.pin_lat},${item.edges[item.edges.length - 1].edge_end.pin_lng}&waypoints=${waypoints}&key=${apiKey}`
      );

      const routes = response.data.routes;
      if (routes.length > 0) {
        const points = routes[0].overview_polyline.points;
        const decodedPoints = decodePolyline(points);

        const polylineCoordinates = decodedPoints.map((point) => ({
          latitude: point[0],
          longitude: point[1],
        }));

        setRouteCoordinates(polylineCoordinates);
      }
    } catch (error) {
      console.warn('Erro ao obter as direções:', error);
    }
  };

  const decodePolyline = (encoded) => {
    const len = encoded.length;
    let index = 0;
    const array = [];
    let lat = 0;
    let lng = 0;

    while (index < len) {
      let b;
      let shift = 0;
      let result = 0;

      do {
        b = encoded.charCodeAt(index++) - 63;
        result |= (b & 0x1f) << shift;
        shift += 5;
      } while (b >= 0x20);

      const dlat = (result & 1) !== 0 ? ~(result >> 1) : result >> 1;
      lat += dlat;

      shift = 0;
      result = 0;

      do {
        b = encoded.charCodeAt(index++) - 63;
        result |= (b & 0x1f) << shift;
        shift += 5;
      } while (b >= 0x20);

      const dlng = (result & 1) !== 0 ? ~(result >> 1) : result >> 1;
      lng += dlng;

      array.push([lat * 1e-5, lng * 1e-5]);
    }

    return array;
  };

  useEffect(() => {
    const requestLocationPermission = async () => {
      try {
        const { status } = await Location.requestForegroundPermissionsAsync();
        const location = await Location.getCurrentPositionAsync({});
        setCurrentLocation({
          latitude: location.coords.latitude,
          longitude: location.coords.longitude,
        });
        setMapRegion({
          latitude: location.coords.latitude,
          longitude: location.coords.longitude,
          latitudeDelta: 0.02,
          longitudeDelta: 0.02,
        });
        setCurrentLocationSet(true);

      } catch (err) {
        console.warn(err);
      }
    };

    requestLocationPermission();
  }, []);

  const handleRegionChange = (region) => {
    if (currentLocation && !currentLocationSet) {
      setCurrentLocationSet(true);
      setMapRegion(region);
      
    }
  };

  return (
    <View style={styles.container}>
      <MapView
        style={styles.map}
        initialRegion={{
          latitude: 39.3999,
          longitude: -8.2245,
          latitudeDelta: 8,
          longitudeDelta: 8,
        }}
        region={mapRegion}
        onRegionChange={handleRegionChange}
      >
        {currentLocation && (
          getDirections(),
          <Circle
            center={currentLocation}
            radius={50} 
            fillColor="rgba(0, 0, 0, 0.6)" 
            strokeColor="rgba(0, 0, 0, 0.8)" 
            strokeWidth={2} 
          />
        )}

        {item.edges.map((edge) => (
          <Marker
            key={edge.edge_start.pin_name}
            coordinate={{
              latitude: edge.edge_start.pin_lat,
              longitude: edge.edge_start.pin_lng,
            }}
            onPress={() => handleMarkerPress(edge.edge_start.pin_name)}
          />
        ))}

        <Marker
          key={item.edges[item.edges.length - 1].edge_end.pin_name}
          coordinate={{
            latitude: item.edges[item.edges.length - 1].edge_end.pin_lat,
            longitude: item.edges[item.edges.length - 1].edge_end.pin_lng,
          }}
          onPress={() => handleMarkerPress(item.edges[item.edges.length - 1].edge_end.pin_name)}
        />

        {routeCoordinates.length > 0 && (
          <Polyline
            coordinates={routeCoordinates}
            strokeColor="rgba(0, 0, 0, 0.8)"
            strokeWidth={2}
          />
        )}
      </MapView>
      
      <Button title="Trails" onPress={handleTrails} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  map: {
    flex: 1,
  },
});

export default MapsScreen;
