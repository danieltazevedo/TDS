import React, { useState, useEffect } from 'react';
import { View, Button,Text, FlatList, Image,TouchableOpacity , StyleSheet} from 'react-native';
import call from 'react-native-phone-call';
import Icon from 'react-native-vector-icons/FontAwesome';


const TrailsActivity =  ({ navigation }) => {
  const [trails, setTrails] = useState([]);

  const handleMoreInfo = (item) => {
    navigation.navigate('TrailInfo', { item });
  };

  const handleCallEmergency = () => {
    const phoneNumber = '112';
    const args = {
      number: phoneNumber,
      prompt: false,
    };
    call(args).catch(error => {
      ToastAndroid.show('Failed to make the call!', ToastAndroid.SHORT);
      console.error(error);
    });
  };

  useEffect(() => {
    fetchTrails();
  }, []);

  const fetchTrails = async () => {
    try {
      const response = await fetch('https://c5a2-193-137-92-29.eu.ngrok.io/trails');
      const data = await response.json();
      setTrails(data);
    } catch (error) {
      console.error(error);
    }
  };


  const renderTrailItem = ({ item }) => (
    <View style={{ flexDirection: 'column', alignItems: 'center' }}>
      <View style={{ flexDirection: 'row', alignItems: 'center' }}>
        <Image
          source={{ uri: item.trail_img }} 
          style={{ width: 50, height: 70, margin: 10 }}
        />
        <Text style={{ fontSize: 20, margin: 16 }}>{item.trail_name}</Text>
      </View>
      <Button title="More Info" onPress={() => handleMoreInfo(item)} />
    </View>
  );

  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <FlatList
        data={trails}
        renderItem={renderTrailItem}
        keyExtractor={(item) => item.id}
      />
      <View style={styles.emergencyButtonContainer}>
      <TouchableOpacity onPress={handleCallEmergency} style={styles.emergencyButton}>
        <Icon name="phone" size={24} color="red" style={styles.emergencyIcon} />
      </TouchableOpacity>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  emergencyButtonContainer: {
    position: 'absolute',
    bottom: 16,
    right: 16,
  },
  emergencyIcon: {
    marginRight: 10,
  },
});

export default TrailsActivity;
