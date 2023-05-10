import React, { useEffect, useState } from 'react';
import { FetchStatus, Track, useTrackList } from './tracklist';
import { AudioPlayer } from './AudioPlayer';

export function App() {
    const [trackList, setTrackList] = useState<Track[]>([])

    useEffect(() => {
        fetch("/api/track-list")
            .then(response => response.json())
            .then(data => setTrackList(data))
    }, [])

    return (
        <>
            <h1>Sleepy Stories</h1>
            <AudioPlayer trackList={trackList} />
        </>
    );
}

const renderFetch = () => {
    return <div>Fetching track list...</div>
}

const renderComplete = (trackList: Track[]) => {
    return <AudioPlayer trackList={trackList} />
}

const renderError = (error: Error) => {
    return <div>Error: {error.message}</div>
}

const TrackListStatus =
    (
        status: FetchStatus<Track[]>,
        renderFetch: () => JSX.Element,
        renderComplete: (tracks: Track[]) => JSX.Element,
        renderError: (error: Error) => JSX.Element
    ) => {

        switch (status.status) {
            case "idle":
                return <></>
                break;
            case "fetching":
                return renderFetch()
            case "complete":
                return renderComplete(status.data)
            case "error":
                return renderError(status.error)
        }

    }