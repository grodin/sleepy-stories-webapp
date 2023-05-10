import { useEffect, useState } from "react"


export type Track = { id: string, url: URL }

export const useTrackList = () => {
    const [status, setStatus] = useState<FetchStatus<Track[]>>({ status: "idle" })

    useEffect(() => {
        const fetchTrackList = async () => {
            setStatus({status: "fetching"})
            const response = await fetch("/api/track-list")
            const trackList = await response.json()
            setStatus({status:"complete", data: trackList})
        }
    })

    return status
}

export type FetchStatus<T> = {status: "idle"} | { status: "fetching" } | { status: "complete", data: T } | { status: "error", error: Error }