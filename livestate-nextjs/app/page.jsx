import UserList from '@/components/UserList';

export default async function Home() {
  const initialUsers = await fetch('http://localhost:8080/api/users', { cache: 'no-store' })
    .then(res => res.ok ? res.json() : [])
    .catch(() => []);

  return (
    <div>
      <h1>Hello world! (Y me la pela)</h1>
      <UserList initialUsers={initialUsers}/>
    </div>
  );
}
