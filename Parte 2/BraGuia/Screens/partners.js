import React, { useEffect, useState } from 'react';
import { View, Text, Button, FlatList, StyleSheet } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';

const Partners = ({ navigation }) => {
  const [dataList, setDataList] = useState([]);

  const handleBack = () => {
    navigation.navigate('PaginaInicial');
  };

  useEffect(() => {
    loadDataFromCache();
  }, []);

  const loadDataFromCache = async () => {
    try {
      const cachedJson = await AsyncStorage.getItem('json_cache');
      if (cachedJson) {
        processJson(cachedJson);
      }
    } catch (error) {
      console.error(error);
    }
  };

  const processJson = (json) => {
    try {
      const jsonObject = JSON.parse(json);
      const partnersArray = jsonObject[0].partners;
      setDataList(partnersArray);
    } catch (error) {
      console.error(error);
    }
  };

  const renderItem = ({ item }) => (
    <View style={{ padding: 10 }}>
      <Text style={{ fontSize: 15, color: '#4B4A4A' }}>{item.partner_name}</Text>
      <Text style={{ fontSize: 15, color: '#4D4A4A', marginTop: 5 }}>{"Phone Number: " + item.partner_phone + "\nE-Mail: " + item.partner_mail + "\n" + item.partner_url}</Text>
    </View>
  );

  return (
    <View style={styles.container}>
      <FlatList
        data={dataList}
        renderItem={renderItem}
        keyExtractor={(item) => item.social_name}
      />
      <View style={styles.buttonContainer}>
        <Button title="Back" onPress={handleBack} />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  buttonContainer: {
    alignSelf: 'center',
    marginBottom: 10,
  },
});

export default Partners;
