import React, { useEffect, useRef, useState } from 'react';


export function AudioPlayer({ trackList }: AudioPlayerProps) {
    const [playerState, setPlayerState] = useState<PlayerState>(READY);
    const player = useRef<HTMLAudioElement>(null);

    useEffect(() => {
        if (playerState.kind == "PLAYING") player.current?.play()
    })

    if (trackList.length == 0) 
        return (<></>)

    let src;

    if (playerState.kind != "FINISHED")
        src = trackList[playerState.trackIndex].toString()
    else
        src = ""

    return (
        <audio ref={player} controls
            src={src}
            preload='auto'
            onEnded={
                () => {
                    if (playerState.kind != "READY") {
                        setPlayerState(nextTrack(trackList, playerState))
                    }
                }
            }
            onPlay={() => {
                if (playerState.kind == "READY")
                    setPlayerState(playing(0))
            }}
        />
    );
}




type PlayerState = Ready | Playing | Finished

interface Ready { kind: "READY", trackIndex: 0 }
const READY: Ready = { kind: "READY", trackIndex: 0 }

interface Playing {
    kind: "PLAYING", trackIndex: number
}
const playing = (trackIndex: number): Playing => { return { kind: "PLAYING", trackIndex } }


interface Finished { kind: "FINISHED", trackIndex: null }
const FINISHED: Finished = { kind: "FINISHED", trackIndex: null }


function nextTrack<T>(trackList: T[], state: Ready | Playing | Finished): PlayerState {
    if (state.kind == "PLAYING") {
        if (state.trackIndex >= trackList.length - 1) {
            return FINISHED;
        } else {
            return playing(state.trackIndex + 1)
        }
    } else if (state.kind == "FINISHED") { return FINISHED }
    else { throw new Error("Should not be able to call nextTrack with Ready state") }
}


export interface AudioPlayerProps {
    trackList: URL[]
}