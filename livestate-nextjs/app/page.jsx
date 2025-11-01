import UserList from "@/components/UserList";
import UserInfo from "@/components/UserInfo";
import { cookies } from "next/headers";
import { fetchData } from "@/lib/server-util";

export default async function Home() {
  const cookieStore = await cookies();
  const authToken = cookieStore.get("token")?.value;

  const username = "Alice";

  const initialUsers = await fetchData("/api/users", {
    authToken,
    defaultValue: [],
  });

  const initialUserData = await fetchData(`/api/user/${username}`, {
    authToken,
    defaultValue: {},
  });

  return (
    <div>
      <h1 className="text-4xl font-bold">Hello world! (Y me la pela)</h1>
      <UserList authToken={authToken} initialUsers={initialUsers} />
      <UserInfo
        authToken={authToken}
        username={username}
        initialUserData={initialUserData}
      />
    </div>
  );
}
