import http from 'k6/http';
import {check, sleep} from 'k6';
import {uuidv4} from 'https://jslib.k6.io/k6-utils/1.4.0/index.js';

export const options = {
  scenarios: {
    steady: {
      executor: 'constant-arrival-rate',
      rate: 200,              // 초당 1000 요청 (원하는 목표로 조정)
      timeUnit: '1s',
      duration: '8m',        // 최소 10분 유지
      preAllocatedVUs: 600,   // 부족하면 k6가 VU를 못 만들어 rate 못 맞춥니다
      maxVUs: 1500,
    },
  },
  thresholds: {
    http_req_duration: ['p(95)<2000'],
    http_req_failed: ['rate<0.01'],
  },
};

const BASE_URL = __ENV.BASE_URL;

const BASE_PAYLOAD = {
  consumerId: "b63d1145-2ff9-4dcb-a387-ebdd435113d2",
  latitude: 37.123456,
  longitude: 127.987654,
  orderProducts: [
    {productId: "08bda1d6-e456-4f14-9651-f9f83d77148d", quantity: 1},
  ],
};

// grafana 설정
const VERSION = __ENV.VERSION;
const GRAFANA_URL = __ENV.GRAFANA_URL;
const GRAFANA_DASHBOARD_ID = 1
const SCRAPE_INTERVAL = 5

function postAnnotation(text, tags = []) {
  const payload = {
    dashboardId: GRAFANA_DASHBOARD_ID,
    text: `[${VERSION}] ${text}`,
    tags: [...tags, `version:${VERSION}`],
    time: Date.now()
  };

  const res = http.post(`${GRAFANA_URL}/api/annotations`,
      JSON.stringify(payload), {
        headers: {
          'Content-Type': 'application/json', timeout: '3s'
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

const test_headers = {
  'Content-Type': 'application/json',
  'Authorization': 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiYXV0aCI6Ik1BU1RFUiIsImlzcyI6Ik92TyIsImlhdCI6MTc0MjQ2OTg0NywiZXhwIjoxODUzMDc0NjQ3fQ.PFGJcPeUK9cqz0X7p_0NjT7LD3GudsJZ1nq2rJYU6rECyJU4QJcJye2NzF9KYZbDDlCKPKYAR3O00VR2gNLevA',
  'X-USER-PASSPORT': '{\\"userId\\":1,\\"userRole\\":\\"MASTER\\"}'
};

export default function () {
  const payload = {...BASE_PAYLOAD, orderId: uuidv4()};

  const res = http.post(
      `${BASE_URL}api/hub/product/confirm-stock`,
      JSON.stringify(payload),
      {headers: test_headers, timeout: '10s'}
  );

  check(res, {
    'status is 202': (r) => r.status === 202,
  });
}