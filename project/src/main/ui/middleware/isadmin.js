export default function ({ $auth, redirect }) {
  if (!$auth.hasScope("ADMIN")) {
    return redirect('/')
  }
}
