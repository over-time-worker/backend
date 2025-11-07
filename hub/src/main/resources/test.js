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
      "productId": "08bda1d6-e456-4f14-9651-f9f83d77148d",
      "quantity": 1
    },
    {
      "productId": "523fb2fd-57e4-400b-b063-c15e4aebcf88",
      "quantity": 1
    },
    {
      "productId": "0c0ff925-0cfc-4b54-9945-f2713d1b673f",
      "quantity": 1
    }
  ]
};
const test_headers = {
  'Content-Type': 'application/json',
  'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6Ik1BU1RFUiIsImlzcyI6Ik92TyIsImlhdCI6MTc2MjE2Mjk4OSwiZXhwIjoxNzYyNzY3Nzg5fQ.tbHn6-VfSE_BwsAz-vHoHVLSLbnHoVawc2jMgMwt-cETjy2q-ELSEtGvEvVA8V1kqypyuQKdIqzUmRqaB1eefw'
};

// grafana 설정
const GRAFANA_URL = 'http://admin:admin@localhost:3000'
const GRAFANA_DASHBOARD_ID = 1
const SCRAPE_INTERVAL = 5

function postAnnotation(text, tags = []) {
  const payload = {
    dashboardId: GRAFANA_DASHBOARD_ID,
    text,
    tags,
    time: Date.now()
  }
  const res = http.post(`${GRAFANA_URL}/api/annotations`,
      JSON.stringify(payload), {
        headers: {
          'Content-Type': 'application/json'
        },
      });

  if (res.status >= 300) {
    console.log(`Failed to post annotation: ${res.status} ${res.body}`)
  }
}

// 테스트 시작 시 어노테이션
export function setup() {
  postAnnotation('부하 테스트 시작', ['load test', 'start'])
}

export function teardown() {
  sleep(SCRAPE_INTERVAL)
  postAnnotation('부하 테스트 종료', ['load test', 'end'])
}

export default function () {
  // POST 요청 발송
  const response = http.post(
      `${BASE_URL}/hub/product/confirm-stock`,
      JSON.stringify(ORDER_PAYLOAD),
      {headers: test_headers}
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

