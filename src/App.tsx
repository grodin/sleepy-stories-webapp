import React from 'react';
import sample from 'url:./media/sample.mp3';

export function App() {
    return (

        <><h1>Sleepy Stories</h1><audio controls src={sample} /></>
    );
}
