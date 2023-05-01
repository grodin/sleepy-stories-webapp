import React from 'react';
import { trackList } from './tracklist';
import { AudioPlayer } from './AudioPlayer';

export function App() {
    return (
        <>
            <h1>Sleepy Stories</h1>
            <AudioPlayer trackList={trackList} />
        </>
    );
}
