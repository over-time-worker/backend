import http from 'k6/http';
import {check, sleep} from 'k6';
import {uuidv4} from 'https://jslib.k6.io/k6-utils/1.4.0/index.js';

// 테스트 설정
export const options = {
  scenarios: {
    limited_requests: {
      executor: 'shared-iterations',
      vus: 1000,              // 동시에 100명의 가상 사용자가 실행됨
      iterations: 1000,      // 총 1000회의 요청만 실행
      maxDuration: '1m30s',    // 1분30초 내에 테스트가 끝나도록 설정 (옵션)
    },
  },
  thresholds: {
    http_req_duration: ['p(95)<2000'],
    http_req_failed: ['rate<0.01'],
  },
};

// 요청에 사용할 기본 URL (실제 API 주소로 변경 필요)
const BASE_URL = 'http://localhost:19081/api';

// 모든 가상 사용자가 동일한 요청 본문 사용
const ORDER_PAYLOAD = {
  "orderId": uuidv4(),
  "consumerId": "b63d1145-2ff9-4dcb-a387-ebdd435113d2",
  "latitude": 37.123456,
  "longitude": 127.987654,
  "orderProducts": [
    {
      "productId": "efc50122-10d5-4bf8-8ee1-7ba60fcd20c6",
      "quantity": 1
    },
    {
      "productId": "8737dc22-482e-4ebe-b870-72818e7adf4e",
      "quantity": 1
    },
    {
      "productId": "566e78da-0ea2-4d44-bf38-6ec273562bdf",
      "quantity": 1
    }
  ]
};
const headers = {
  'Content-Type': 'application/json',
  'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6Ik1BU1RFUiIsImlzcyI6Ik92TyIsImlhdCI6MTc2MjE2Mjk4OSwiZXhwIjoxNzYyNzY3Nzg5fQ.tbHn6-VfSE_BwsAz-vHoHVLSLbnHoVawc2jMgMwt-cETjy2q-ELSEtGvEvVA8V1kqypyuQKdIqzUmRqaB1eefw'
};

export default function () {
  // POST 요청 발송
  const response = http.post(
      `${BASE_URL}/hub/product/confirm-stock`,
      JSON.stringify(ORDER_PAYLOAD),
      {headers: headers}
  );

  // 응답 확인 (확장된 검증)
  check(response, {
    'status is 202': (r) => r.status === 202,
    'response body has data': (r) => r.json().hasOwnProperty('data'),
    'products array exists': (r) => r.json().data.products && Array.isArray(
        r.json().data.products),
    'expected product count': (r) => r.json().data.products.length
        === ORDER_PAYLOAD.orderProducts.length
  });

  // 응답 데이터 로깅 (선택사항)
  if (response.status !== 202) {
    console.log(`Failed response: ${response.body}`);
  }

  sleep(1);
}

