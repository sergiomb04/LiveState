import UserList from "@/components/UserList";
import PublishButton from "@/components/PublishButton";
import { cookies } from "next/headers";
import { fetchData } from "livestate-nextjs";
import FakePlayer from "@/components/FakePlayer";
import UserInfo from "@/components/UserInfo";

export default async function Home() {
  const cookieStore = await cookies();
  const authToken = cookieStore.get("token")?.value;

  const username = "Bob";

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
    <div className="flex flex-col justify-center align-middle text-center m-auto h-screen gap-8">
      <h1 className="text-4xl font-bold">Hello world! 👋</h1>
      <div className="flex gap-4 w-full justify-center">
        <UserList authToken={authToken} initialUsers={initialUsers} />
        <UserInfo
          authToken={authToken}
          username={username}
          initialUserData={initialUserData}
        />
      </div>
      <div className="flex flex-col gap-4 justify-center items-center">
        <FakePlayer authToken={authToken} initialData={initialFakePlayerData} />
        <PublishButton />
      </div>
    </div>
  );
}
