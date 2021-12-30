export default function ({ $auth, redirect }) {
  if (!($auth.hasScope("STUDY_DEPARTMENT_EMPLOYEE") || $auth.hasScope("ADMIN"))) {
    return redirect('/')
  }
}
