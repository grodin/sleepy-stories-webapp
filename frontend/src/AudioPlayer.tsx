import React, { useEffect, useRef, useState } from 'react';
import { Track } from './tracklist';


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
        src = trackList[playerState.trackIndex].url.toString()
    else
        src = ""

    return (
        <div>
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
            <div>
                {trackList[playerState.trackIndex].id}: {trackList[playerState.trackIndex].url.toString()}
            </div>
        </div>
    );
}




type PlayerState = Ready | Playing | Finished

interface Ready { kind: "READY", trackIndex: 0 }
const READY: Ready = { kind: "READY", trackIndex: 0 }

interface Playing {
    kind: "PLAYING", trackIndex: number
}
const playing = (trackIndex: number): Playing => { return { kind: "PLAYING", trackIndex } }


type Finished = { kind: "FINISHED", trackIndex: number }
const finished = (trackList: any[]): Finished => { return { kind: "FINISHED", trackIndex: trackList.length - 1 } }


function nextTrack<T>(trackList: T[], state: Ready | Playing | Finished): PlayerState {
    if (state.kind == "PLAYING") {
        if (state.trackIndex >= trackList.length - 1) {
            return finished(trackList);
        } else {
            return playing(state.trackIndex + 1)
        }
    } else if (state.kind == "FINISHED") { return state }
    else { throw new Error("Should not be able to call nextTrack with Ready state") }
}


export interface AudioPlayerProps {
    trackList: Track[]
}