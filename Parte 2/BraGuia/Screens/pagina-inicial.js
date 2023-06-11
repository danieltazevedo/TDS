import React, { useEffect, useState } from 'react';
import { View, Text, Button, StyleSheet } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';

const PaginaInicial = ({ navigation }) => {
  const [appName, setAppName] = useState('');
  const [descricao, setDescricao] = useState('');
  const [pageText, setPageText] = useState('');

  useEffect(() => {
    checkCache();
  }, []);

  const checkCache = async () => {
    try {
      const cachedJson = await AsyncStorage.getItem('json_cache');
      if (cachedJson) {
        const data = JSON.parse(cachedJson);
        processJson(data);
      } else {
        fetchJson();
      }
    } catch (error) {
      console.log('Error reading cache:', error);
    }
  };

  const fetchJson = async () => {
    try {
      const response = await fetch('https://c5a2-193-137-92-29.eu.ngrok.io/app');
      const data = await response.json();
      processJson(data);
      cacheJson(data);
    } catch (error) {
      console.log('Error fetching JSON:', error);
    }
  };

  const cacheJson = async (data) => {
    try {
      const json = JSON.stringify(data);
      await AsyncStorage.setItem('json_cache', json);
    } catch (error) {
      console.log('Error caching JSON:', error);
    }
  };

  const processJson = (data) => {
    const jsonObject = data[0];
    setAppName(jsonObject.app_name);
    setDescricao(jsonObject.app_desc);
    setPageText(jsonObject.app_landing_page_text);
  };

  const handleLogin = () => {
    navigation.navigate('Login');
  };

  return (
    <View style={styles.container}>
      <Text style={styles.appName}>{appName}</Text>
      <Text style={styles.descricao}>{descricao}</Text>
      <Text style={styles.pageText}>{pageText}</Text>
      <View style={styles.guideline} />

      <Button title="Login" onPress={handleLogin} />

      <View style={styles.buttonsContainer}>
        <Button title="Contacts" />
        <Button title="Partners" />
        <Button title="Socials" />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  appName: {
    fontSize: 40,
    textAlign: 'center',
    marginBottom: 16,
  },
  descricao: {
    fontSize: 20,
    textAlign: 'center',
    marginBottom: 16,
  },
  pageText: {
    fontSize: 10,
    textAlign: 'center',
    marginBottom: 16,
  },
  guideline: {
    flex: 1,
    width: '100%',
  },
  buttonsContainer: {
    flexDirection: 'row',
    justifyContent: 'center',
    marginTop: 16,
  },
});

export default PaginaInicial;
