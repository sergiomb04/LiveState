"use client"

import { useRealtimeState } from '@/lib/realtime';

export default function FakePlayer({authToken, initialData}) {
    const [data] = useRealtimeState('fakePlayer', initialData, authToken);

    return (
        <div>
            <h1>NAME: {data.name}</h1>
            <h1>UUID: {data.uuid}</h1>
        </div>
    )

}