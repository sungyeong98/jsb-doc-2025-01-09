export default async function Page() {
  const response = await fetch("http://localhost:8080/api/v1/question_list");

  if (!response.ok) {
    console.error("API 호출 실패:", response.status, response.statusText);
    return <div>데이터를 불러오는 데 실패했습니다.</div>;
  }

  const body = await response.json();

  return (
    <div>
      <div>
        <div>currentPageNumber: {body.currentPage}</div>

        <div>pageSize: {body.pageSize}</div>

        <div>totalPages: {body.totalPages}</div>

        <div>totalItems: {body.totalItems}</div>
      </div>

      <hr />

      <ul>
        {body.items.map((item: any) => (
          <li key={item.id} className="border-[2px] border-[red] my-3">
            <div>id : {item.id}</div>
            <div>createDate : {item.createDate}</div>
            <div>modifyDate : {item.modifyDate}</div>
            <div>author : {item.author}</div>
            <div>subject : {item.subject}</div>
            <div>published : {item.published}</div>
            <div>listed : {item.listed}</div>
          </li>
        ))}
      </ul>
    </div>
  );
}
