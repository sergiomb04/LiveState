"use client"

import { publish, useRealtimeState } from "livestate-nextjs";
import { useEffect } from "react";

export default function TestChannelButton() {

    const [data] = useRealtimeState("testChannel", "")
    
    useEffect(() => {
        console.log("Received testChannel message", data);        
    }, [data]);

    return (
        <div>
            <p>Nombre: {data.name}</p>
            <button type="button" onClick={() => publish("testChannel", {name:"Nombre-" + (Math.floor(Math.random() * 9000000000000000) + 1000000000000000)})}>TEST CHANNELING</button>
        </div>
    )
}