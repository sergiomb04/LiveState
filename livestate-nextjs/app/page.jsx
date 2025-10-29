import UserList from "@/components/UserList";
import UserInfo from "@/components/UserInfo";
import { cookies } from "next/headers";

export default async function Home() {
  const cookieStore = await cookies();
  const authToken = cookieStore.get("token")?.value;

  const username = "Alice";

  const initialUsers = await fetch("http://localhost:8080/api/users", {
    headers: {
      Authorization: `Bearer ${authToken}`,
    },
    cache: "no-store",
  })
    .then((res) => (res.ok ? res.json() : []))
    .catch(() => []);

    const initialUserData = await fetch(`http://localhost:8080/api/user/${username}`, {
    headers: {
      Authorization: `Bearer ${authToken}`,
    },
    cache: "no-store",
  })
    .then((res) => (res.ok ? res.json() : []))
    .catch(() => []);

  return (
    <div>
      <h1>Hello world! (Y me la pela)</h1>
      <UserList authToken={authToken} initialUsers={initialUsers} />
      <UserInfo authToken={authToken} username={username} initialUserData={initialUserData}/>
    </div>
  );
}
