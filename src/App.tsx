import React from 'react';
import * as audioFiles from 'url:./media/*.mp3'
import { AudioPlayer } from './AudioPlayer';

export function App() {
    return (
        <>
            <h1>Sleepy Stories</h1>
            <AudioPlayer trackList={tracks} />
        </>
    );
}

const tracks = Object.keys(audioFiles).map((key) => { return audioFiles[key as keyof typeof audioFiles] });