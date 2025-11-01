import UserList from "@/components/UserList";
import TestUnmount from "@/components/TestUnmount";
import { cookies } from "next/headers";
import { fetchData } from "@/lib/server-util";
import FakePlayer from '@/components/FakePlayer';

export default async function Home() {
  const cookieStore = await cookies();
  const authToken = cookieStore.get("token")?.value;

  const username = "Sergio";

  const initialUsers = await fetchData("/api/users", {
    authToken,
    defaultValue: [],
  });

  const initialUserData = await fetchData(`/api/user/${username}`, {
    authToken,
    defaultValue: {},
  });

  const initialFakePlayerData = await fetchData(`/api/fakePlayer`, {
    authToken,
    defaultValue: {},
  });

  return (
    <div>
      <h1 className="text-4xl font-bold">Hello world! (Y me la pela)</h1>
      <UserList authToken={authToken} initialUsers={initialUsers} />
      <TestUnmount authToken={authToken} username={username} initialUserData={initialUserData}/>
      <FakePlayer authToken={authToken} initialData={initialFakePlayerData}/>
    </div>
  );
}
