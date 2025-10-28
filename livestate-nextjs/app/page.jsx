import UserList from '@/components/UserList';

export default async function Home() {
  const res = await fetch('http://localhost:8080/api/users', { cache: 'no-store' });
  const initialUsers = await res.json();
  return (
    <div>
      <h1>Hello world! (Y me la pela)</h1>
      <UserList initialUsers={initialUsers}/>
    </div>
  );
}
