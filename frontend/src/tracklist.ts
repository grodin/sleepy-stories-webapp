import { useEffect, useState } from "react"


export type Track = { id: string, url: URL, mediaMetadata: { title: string, artist: string, album: string, }, }

export const useTrackList = () => {
    const [status, setStatus] = useState<FetchStatus<Track[]>>({ status: "idle" })

    useEffect(() => {
        setStatus({ status: "fetching" })
        fetch("/api/track-list")
            .then(response => response.json())
            .then(trackList =>
                setStatus({ status: "complete", data: trackList }))
    })

    return status
}

export type FetchStatus<T> = { status: "idle" } | { status: "fetching" } | { status: "complete", data: T } | { status: "error", error: Error }