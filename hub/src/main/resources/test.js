import http from 'k6/http';
import { check, sleep } from 'k6';
import { SharedArray } from 'k6/data';

// 테스트 설정
export const options = {
  scenarios: {
    limited_requests: {
      executor: 'shared-iterations',
      vus: 10,              // 동시에 10명의 가상 사용자가 실행됨
      iterations: 500,      // 총 500회의 요청만 실행
      maxDuration: '1m',    // 1분 내에 테스트가 끝나도록 설정 (옵션)
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
  "consumerId": "b63d1145-2ff9-4dcb-a387-ebdd435113d2",
  "latitude": 37.123456,
  "longitude": 127.987654,
  "orderProducts": [
    {
      "productId": "a31c337f-d9c9-4ab3-8820-6a901a9d5cd0",
      "quantity": 1
    },
    {
      "productId": "03e0ee67-1f14-47b7-9f9c-0a8c5e9497a3",
      "quantity": 1
    },
    {
      "productId": "9d12d108-1368-4065-9b59-2768f87dcc45",
      "quantity": 1
    }
  ]
};
const headers = {
  'Content-Type': 'application/json',
  'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6Ik1BU1RFUiIsImlzcyI6Ik92TyIsImlhdCI6MTc0MjQ2OTg0NywiZXhwIjoxNzQzMDc0NjQ3fQ.6s1NGmWkBEF5AbYbOgwkzI0uMiLuC7zqXVsbkLPkhWRR_OWx9XcdwppZcS64w02I3v8wg_xKXKns_GcU-twkAg'
};


export default function() {  
  // POST 요청 발송
  const response = http.post(
      `${BASE_URL}/hub/product/confirm-stock`,
      JSON.stringify(ORDER_PAYLOAD),
      { headers: headers }
  );
  
  // 응답 확인 (확장된 검증)
  check(response, {
    'status is 202': (r) => r.status === 202,
    'response body has data': (r) => r.json().hasOwnProperty('data'),
    'products array exists': (r) => r.json().data.products && Array.isArray(r.json().data.products),
    'expected product count': (r) => r.json().data.products.length === ORDER_PAYLOAD.orderProducts.length
  });
  
  // 응답 데이터 로깅 (선택사항)
  if (response.status !== 202) {
    console.log(`Failed response: ${response.body}`);
  }

  sleep(1);
}

