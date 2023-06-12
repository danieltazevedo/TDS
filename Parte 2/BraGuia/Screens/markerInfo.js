import React, { useEffect, useState } from 'react';
import { View, Text, ScrollView, Image,Button } from 'react-native';
import WebView from 'react-native-webview';
import { Audio } from 'expo-av';

const MarkerInfo = ({ route }) => {
  const [soundObject, setSoundObject] = useState(null);
  const { item } = route.params;
  
  const getImageURL = (item) => {
    let imageURL = null;
    let hasImage = false;
  
    if (item.media) {
      for (const media of item.media) {
        if (media.media_type === "I") {
          imageURL = media.media_file;
          hasImage = true;
          break;
        }
      }
    }
  
    return { imageURL, hasImage };
  };

  const getVideoURL = (item) => {
    let videoURL = null;
    let hasVideo = false;
  
    if (item.media) {
      for (const media of item.media) {
        if (media.media_type === "V") {
          videoURL = media.media_file;
          hasVideo = true;
          break;
        }
      }
    }
  
    return { videoURL, hasVideo };
  };

  const getAudioURL = (item) => {
    let audioURL = null;
    let hasAudio = false;
  
    if (item.media) {
      for (const media of item.media) {
        if (media.media_type === "R") {
            audioURL = media.media_file;
            hasAudio = true;
          break;
        }
      }
    }
  
    return { audioURL, hasAudio };
  };

  const { videoURL, hasVideo } = getVideoURL(item);
  const { imageURL, hasImage } = getImageURL(item);
  const { audioURL, hasAudio } = getAudioURL(item);

  const playAudio = async () => {
    const soundObject = new Audio.Sound();
  
    try {
      setSoundObject(soundObject)
      await soundObject.loadAsync({ uri: audioURL });
      await soundObject.playAsync();
    } catch (error) {
      console.log('Erro ao reproduzir o áudio', error);
    }
  };
  
  
  const pauseAudio = async () => {
    if (soundObject) {
      await soundObject.pauseAsync();
    }
  };
   

  return (
    <ScrollView>
      <View>
        <Text style={{ fontSize: 40, textAlign: 'center' }}>{item.pin_name}</Text>
        <Text style={{ fontSize: 20, textAlign: 'center' }}>{item.pin_desc}</Text>
        {hasImage && (
          <Image style={{ width: 114, height: 120, margin: 20, alignSelf: 'center' }} source={{ uri: imageURL }} />
        )}
        {hasVideo && (
          <WebView
            style={{ width: 297, height: 200, alignSelf: 'center' }}
            source={{ uri: videoURL }}
            allowsInlineMediaPlayback
            mediaPlaybackRequiresUserAction={false}
          />
        )}
      {hasAudio && (
          <View>
            <Button title="Play Audio" onPress={playAudio} />
            <Button title="Pause Audio" onPress={pauseAudio} />
          </View>
        )}
      </View>
    </ScrollView>
  );
};

export default MarkerInfo;
