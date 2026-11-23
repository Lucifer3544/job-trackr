
import { useEffect, useState } from 'react';
import './responsive-theme-overrides.css';
import {
  NavLink,
  Route,
  Routes,
  useLocation
} from 'react-router-dom';
import {
  Activity as ActivityIcon,
  BriefcaseBusiness,
  Building2,
  CalendarDays,
  ChartNoAxesCombined,
  ChevronDown,
  CircleHelp,
  Clock3,
  Handshake,
  LayoutDashboard,
  Menu,
  Plus,
  Search,
  Settings,
  Users,
  Wallet,
  X,
  Trash2,
  RefreshCw,
  MapPin,
  ArrowUpRight,
  Sun,
  Moon
} from 'lucide-react';
import {
  Bar,
  BarChart,
  CartesianGrid,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis
} from 'recharts';
import { services } from './api/services';
import type {
  Application,
  Company,
  HRContact,
  Interview,
  Offer,
  FollowUp,
  Activity
} from './types';

const nav = [
  ['Overview', '/', LayoutDashboard],
  ['Applications', '/applications', BriefcaseBusiness],
  ['Companies', '/companies', Building2],
  ['HR contacts', '/contacts', Users],
  ['Interviews', '/interviews', CalendarDays],
  ['Offers', '/offers', Handshake],
  ['Follow-ups', '/followups', Clock3],
  ['Activity timeline', '/activity', ActivityIcon]
] as const;

const statusOptions = [
  'APPLIED',
  'SCREENING',
  'ASSESSMENT',
  'INTERVIEW',
  'ON_HOLD',
  'OFFER_RECEIVED',
  'NEGOTIATION',
  'OFFER_ACCEPTED',
  'OFFER_REJECTED',
  'REJECTED',
  'GHOSTED',
  'WITHDRAWN',
  'CLOSED'
];

const money = (v: number) =>
  new Intl.NumberFormat('en-IN', {
    style: 'currency',
    currency: 'INR',
    maximumFractionDigits: 0
  }).format(v || 0);

const date = (v?: string) =>
  v
    ? new Date(v).toLocaleDateString('en-IN', {
        day: '2-digit',
        month: 'short',
        year: 'numeric'
      })
    : '—';

const title = (s: string) =>
  s
    .toLowerCase()
    .replaceAll('_', ' ')
    .replace(/\b\w/g, c => c.toUpperCase());

function App() {
  const [collapsed, setCollapsed] = useState(false);
  const [theme, setTheme] = useState<'light' | 'dark'>(
    () =>
      (localStorage.getItem('jobtrackr-theme') as 'light' | 'dark') ||
      'light'
  );
  const [mobileOpen, setMobileOpen] = useState(false);

  const [apps, setApps] = useState<Application[]>([]);
  const [companies, setCompanies] = useState<Company[]>([]);
  const [contacts, setContacts] = useState<HRContact[]>([]);
  const [interviews, setInterviews] = useState<Interview[]>([]);
  const [offers, setOffers] = useState<Offer[]>([]);
  const [followups, setFollowups] = useState<FollowUp[]>([]);
  const [activities, setActivities] = useState<Activity[]>([]);
  const [dashboard, setDashboard] = useState<any>(null);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const location = useLocation();

  const refresh = async () => {
    setLoading(true);
    setError('');

    try {
      const [c, a, d] = await Promise.all([
        services.companies(),
        services.applications(),
        services.dashboard()
      ]);

      setCompanies(c);
      setApps(a);
      setDashboard(d);

      const [h, i, o, f, t] = await Promise.all([
        services.allContacts(c),
        services.interviews(a),
        services.offers(a),
        services.allFollowups(a),
        services.activities(a)
      ]);

      setContacts(h);
      setInterviews(i);
      setOffers(o);
      setFollowups(f);
      setActivities(t);
    } catch (e) {
      setError(
        e instanceof Error
          ? e.message
          : 'Unable to load data. Is the backend running?'
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    refresh();
  }, []);

  useEffect(() => {
    document.documentElement.dataset.theme = theme;
    localStorage.setItem('jobtrackr-theme', theme);
  }, [theme]);

  const current =
    nav.find(n => n[1] === location.pathname)?.[0] || 'Overview';

  return (
    <div className="shell">
      <button
        aria-label="Close navigation"
        className={`sidebar-backdrop ${mobileOpen ? 'visible' : ''}`}
        onClick={() => setMobileOpen(false)}
      />

      <aside
        className={`sidebar ${collapsed ? 'collapsed' : ''} ${
          mobileOpen ? 'mobile-open' : ''
        }`}
      >
        <div className="brand">
          <div className="brand-mark">J</div>

          {!collapsed && (
            <div>
              <b>JobTrackr</b>
              <small>CAREER WORKSPACE</small>
            </div>
          )}

          <button
            className="icon-btn collapse"
            onClick={() => setCollapsed(!collapsed)}
          >
            <Menu size={18} />
          </button>
        </div>

        <div className="workspace-label">WORKSPACE</div>

        <nav>
          {nav.map(([label, path, Icon]) => (
            <NavLink
              key={path}
              to={path}
              end={path === '/'}
              onClick={() => setMobileOpen(false)}
              className={({ isActive }) =>
                `nav-link ${isActive ? 'active' : ''}`
              }
              title={collapsed ? label : ''}
            >
              <Icon size={18} />
              {!collapsed && <span>{label}</span>}
              {!collapsed && label === 'Applications' && (
                <em>{apps.length}</em>
              )}
            </NavLink>
          ))}
        </nav>

        <div className="sidebar-bottom">
          <button
            className="theme-toggle"
            onClick={() =>
              setTheme(theme === 'light' ? 'dark' : 'light')
            }
            title={
              theme === 'light'
                ? 'Switch to dark mode'
                : 'Switch to light mode'
            }
          >
            {theme === 'light' ? (
              <Moon size={17} />
            ) : (
              <Sun size={17} />
            )}
            <span>
              {theme === 'light' ? 'Dark mode' : 'Light mode'}
            </span>
          </button>

          <div className="profile">
            <div className="avatar">O</div>
            {!collapsed && (
              <div>
                <b>My workspace</b>
                <small>Personal account</small>
              </div>
            )}
          </div>
        </div>
      </aside>

      <main className="main">
        <header className="topbar">
          <button
            className="icon-btn mobile-menu"
            aria-label="Open navigation"
            onClick={() => {
              setCollapsed(false);
              setMobileOpen(true);
            }}
          >
            <Menu size={19} />
          </button>

          <div className="crumb">
            Workspace <span>/</span> <b>{current}</b>
          </div>

          <div className="top-actions">
            <button
              className="icon-btn"
              title="Refresh data"
              onClick={refresh}
            >
              <RefreshCw size={17} />
            </button>
            <div className="top-avatar">O</div>
          </div>
        </header>

        <div className="page-wrap">
          <div className="page-heading">
            <div>
              <div className="eyebrow">YOUR CAREER, ORGANIZED</div>
              <h1>
                {current === 'Overview'
                  ? 'Good to see you again'
                  : current}
              </h1>
              <p>
                {current === 'Overview'
                  ? 'Here’s what’s happening with your job search.'
                  : `Manage and keep track of your ${current.toLowerCase()}.`}
              </p>
            </div>

            {location.pathname !== '/' && (
              <button
                className="primary"
                onClick={() =>
                  window.dispatchEvent(new CustomEvent('open-create'))
                }
              >
                <Plus size={17} />
                Add{' '}
                {current === 'Applications'
                  ? 'application'
                  : current === 'Companies'
                    ? 'company'
                    : current === 'HR contacts'
                      ? 'contact'
                      : current === 'Interviews'
                        ? 'interview'
                        : current === 'Offers'
                          ? 'offer'
                          : 'follow-up'}
              </button>
            )}
          </div>

          {error && (
            <div className="error">
              <b>Could not load your data.</b> {error}
              <button onClick={refresh}>Retry</button>
              <small>
                Check that Spring Boot is running on localhost:8080 and
                CORS allows localhost:5173.
              </small>
            </div>
          )}

          {loading ? (
            <div className="loading">Loading your workspace…</div>
          ) : (
            <Routes>
              <Route
                path="/"
                element={
                  <Dashboard
                    data={dashboard}
                    apps={apps}
                    activities={activities}
                    interviews={interviews}
                    followups={followups}
                  />
                }
              />

              <Route
                path="/applications"
                element={
                  <ApplicationsPage
                    rows={apps}
                    companies={companies}
                    refresh={refresh}
                  />
                }
              />

              <Route
                path="/companies"
                element={
                  <CompaniesPage rows={companies} refresh={refresh} />
                }
              />

              <Route
                path="/contacts"
                element={
                  <ContactsPage
                    rows={contacts}
                    companies={companies}
                    refresh={refresh}
                  />
                }
              />

              <Route
                path="/interviews"
                element={
                  <InterviewsPage
                    rows={interviews}
                    apps={apps}
                    refresh={refresh}
                  />
                }
              />

              <Route
                path="/offers"
                element={
                  <OffersPage rows={offers} apps={apps} refresh={refresh} />
                }
              />

              <Route
                path="/followups"
                element={
                  <FollowupsPage
                    rows={followups}
                    apps={apps}
                    refresh={refresh}
                  />
                }
              />

              <Route
                path="/activity"
                element={<ActivityPage rows={activities} />}
              />
            </Routes>
          )}
        </div>
      </main>
    </div>
  );
}

function Stat({
  label,
  value,
  sub,
  icon: Icon,
  tint = 'blue'
}: any) {
  return (
    <div className="stat-card">
      <div className={`stat-icon ${tint}`}>
        <Icon size={19} />
      </div>
      <div className="stat-label">{label}</div>
      <div className="stat-value">{value ?? 0}</div>
      <div className="stat-sub">{sub}</div>
    </div>
  );
}

function Dashboard({
  data,
  apps,
  activities,
  interviews,
  followups
}: any) {
  const status = data?.applicationsByStatus || {};

  const chart = Object.entries(status).map(([name, value]) => ({
    name: title(name),
    count: value as number
  }));

  const upcomingI = data?.upcomingInterviews || [];
  const upcomingF = data?.upcomingFollowUps || [];

  return (
    <>
      <div className="stats-grid">
        <Stat
          label="Total applications"
          value={data?.totalApplications ?? apps.length}
          sub="Across all companies"
          icon={BriefcaseBusiness}
        />

        <Stat
          label="Interviews"
          value={data?.interviewStatistics?.total ?? interviews.length}
          sub={`${data?.interviewStatistics?.upcoming ?? 0} upcoming`}
          icon={CalendarDays}
          tint="purple"
        />

        <Stat
          label="Offers received"
          value={data?.offerStatistics?.total ?? 0}
          sub={`${data?.offerStatistics?.pending ?? 0} awaiting response`}
          icon={Handshake}
          tint="green"
        />

        <Stat
          label="Pending follow-ups"
          value={
            data?.followUpStatistics?.pending ??
            followups.filter((f: FollowUp) => f.status === 'PENDING').length
          }
          sub="Keep the momentum going"
          icon={Clock3}
          tint="orange"
        />
      </div>

      <div className="dashboard-grid">
        <section className="panel chart-panel">
          <div className="panel-head">
            <div>
              <h2>Application pipeline</h2>
              <p>Distribution by current status</p>
            </div>
            <span className="pill">All time</span>
          </div>

          {chart.length ? (
            <ResponsiveContainer width="100%" height={270}>
              <BarChart
                data={chart}
                margin={{ top: 10, right: 10, left: -20, bottom: 35 }}
              >
                <CartesianGrid
                  strokeDasharray="3 3"
                  vertical={false}
                  stroke="#edf0f5"
                />
                <XAxis
                  dataKey="name"
                  angle={-25}
                  textAnchor="end"
                  interval={0}
                  tick={{ fontSize: 11, fill: '#778197' }}
                  axisLine={false}
                  tickLine={false}
                />
                <YAxis
                  allowDecimals={false}
                  tick={{ fontSize: 11, fill: '#778197' }}
                  axisLine={false}
                  tickLine={false}
                />
                <Tooltip />
                <Bar
                  dataKey="count"
                  fill="#5b67e8"
                  radius={[6, 6, 0, 0]}
                  maxBarSize={42}
                />
              </BarChart>
            </ResponsiveContainer>
          ) : (
            <Empty text="Add your first application to see your pipeline." />
          )}
        </section>

        <section className="panel">
          <div className="panel-head">
            <div>
              <h2>Applications by company</h2>
              <p>Where you’ve applied</p>
            </div>
          </div>

          <div className="company-list">
            {(data?.applicationsByCompany || []).length ? (
              (data.applicationsByCompany as any[])
                .slice(0, 6)
                .map(c => (
                  <div className="company-row" key={c.companyId}>
                    <div className="company-logo">
                      {c.companyName?.slice(0, 1)}
                    </div>
                    <span>{c.companyName}</span>
                    <b>{c.applicationCount}</b>
                  </div>
                ))
            ) : (
              <Empty text="Your company breakdown will appear here." />
            )}
          </div>
        </section>

        <section className="panel">
          <div className="panel-head">
            <div>
              <h2>Upcoming interviews</h2>
              <p>Your next conversations</p>
            </div>
            <CalendarDays size={18} className="muted" />
          </div>

          {upcomingI.length ? (
            upcomingI.map((x: any) => (
              <div className="agenda-row" key={x.id}>
                <div className="date-tile">
                  <b>{new Date(x.scheduledAt).getDate()}</b>
                  <small>
                    {new Date(x.scheduledAt).toLocaleString('en', {
                      month: 'short'
                    })}
                  </small>
                </div>
                <div>
                  <b>{x.companyName}</b>
                  <p>{x.roundName} · {x.jobRole}</p>
                </div>
                <span className="time-label">
                  {new Date(x.scheduledAt).toLocaleTimeString('en-IN', {
                    hour: '2-digit',
                    minute: '2-digit'
                  })}
                </span>
              </div>
            ))
          ) : (
            <Empty text="No upcoming interviews scheduled." />
          )}
        </section>

        <section className="panel">
          <div className="panel-head">
            <div>
              <h2>Follow-up reminders</h2>
              <p>Stay on top of your outreach</p>
            </div>
            <Clock3 size={18} className="muted" />
          </div>

          {upcomingF.length ? (
            upcomingF.map((x: any) => (
              <div className="agenda-row" key={x.id}>
                <div className="date-tile orange-tile">
                  <b>{new Date(x.followUpDate).getDate()}</b>
                  <small>
                    {new Date(x.followUpDate).toLocaleString('en', {
                      month: 'short'
                    })}
                  </small>
                </div>
                <div>
                  <b>{x.companyName}</b>
                  <p>{x.description}</p>
                </div>
                <span className="pill">{title(x.followUpType)}</span>
              </div>
            ))
          ) : (
            <Empty text="No upcoming follow-ups." />
          )}
        </section>
      </div>

      <section className="panel activity-panel">
        <div className="panel-head">
          <div>
            <h2>Recent activity</h2>
            <p>Your latest job search updates</p>
          </div>
          <span className="muted">Last 10 events</span>
        </div>

        {(data?.recentActivities || activities).slice(0, 6).length ? (
          (data?.recentActivities || activities)
            .slice(0, 6)
            .map((x: any) => (
              <div className="activity-row" key={x.id}>
                <div className="activity-dot" />
                <div className="activity-copy">
                  <b>{x.title}</b>
                  <p>{x.companyName} · {x.jobRole}</p>
                </div>
                <time>{date(x.activityDate)}</time>
              </div>
            ))
        ) : (
          <Empty text="Your activity timeline will appear here." />
        )}
      </section>
    </>
  );
}

function Empty({ text }: any) {
  return <div className="empty">{text}</div>;
}

/* Reusable table with dynamic serial numbers */
function TablePage({
  columns,
  rows,
  searchKey = 'name',
  onDelete,
  actions
}: any) {
  const [q, setQ] = useState('');

  const filtered = rows.filter((r: any) =>
    columns.some((c: any) =>
      String(r[c.key] ?? '').toLowerCase().includes(q.toLowerCase())
    )
  );

  return (
    <section className="panel table-panel">
      <div className="table-toolbar">
        <div className="searchbox">
          <Search size={16} />
          <input
            placeholder="Search records..."
            value={q}
            onChange={e => setQ(e.target.value)}
          />
        </div>
        <span>{filtered.length} records</span>
      </div>

      <div className="table-scroll">
        <table>
          <thead>
            <tr>
              <th>S.No.</th>
              {columns.map((c: any) => (
                <th key={c.key}>{c.label}</th>
              ))}
              {(onDelete || actions) && <th>Actions</th>}
            </tr>
          </thead>

          <tbody>
            {filtered.map((r: any, index: number) => (
              <tr key={r.id}>
                <td>{index + 1}</td>

                {columns.map((c: any) => (
                  <td key={c.key}>
                    {c.render ? c.render(r) : r[c.key] ?? '—'}
                  </td>
                ))}

                {(onDelete || actions) && (
                  <td>
                    <div className="row-actions">
                      {actions?.(r)}
                      {onDelete && (
                        <button
                          className="icon-btn danger"
                          onClick={() => onDelete(r)}
                          title="Delete"
                        >
                          <Trash2 size={16} />
                        </button>
                      )}
                    </div>
                  </td>
                )}
              </tr>
            ))}
          </tbody>
        </table>

        {!filtered.length && (
          <Empty text="No records found. Add a record to get started." />
        )}
      </div>
    </section>
  );
}

function useCreate(
  handler: (v: any) => Promise<any>,
  refresh: () => void,
  fields: any[]
) {
  const [open, setOpen] = useState(false);
  const [form, setForm] = useState<any>({});
  const [busy, setBusy] = useState(false);
  const [err, setErr] = useState('');

  useEffect(() => {
    const f = () => {
      setForm({});
      setErr('');
      setOpen(true);
    };

    window.addEventListener('open-create', f);
    return () => window.removeEventListener('open-create', f);
  }, []);

  const modal = (
    <>
      {open && (
        <div
          className="modal-backdrop"
          onMouseDown={e => {
            if (e.target === e.currentTarget) setOpen(false);
          }}
        >
          <form
            className="modal"
            onSubmit={async e => {
              e.preventDefault();
              setBusy(true);
              setErr('');

              try {
                await handler(form);
                setOpen(false);
                refresh();
              } catch (x) {
                setErr(
                  x instanceof Error ? x.message : 'Unable to save'
                );
              } finally {
                setBusy(false);
              }
            }}
          >
            <div className="modal-head">
              <div>
                <h2>Add new record</h2>
                <p>Enter the details below to continue.</p>
              </div>
              <button
                type="button"
                className="icon-btn"
                onClick={() => setOpen(false)}
              >
                <X size={18} />
              </button>
            </div>

            {fields.map(f => (
              <label className="field" key={f.key}>
                {f.label}

                {f.type === 'select' ? (
                  <select
                    required={f.required !== false}
                    value={form[f.key] ?? ''}
                    onChange={e =>
                      setForm({
                        ...form,
                        [f.key]: f.cast
                          ? f.cast(e.target.value)
                          : e.target.value
                      })
                    }
                  >
                    <option value="">
                      Select {f.label.toLowerCase()}
                    </option>
                    {f.options.map((o: any) => (
                      <option
                        value={o.value ?? o}
                        key={o.value ?? o}
                      >
                        {o.label ?? title(o)}
                      </option>
                    ))}
                  </select>
                ) : (
                  <input
                    required={f.required !== false}
                    type={f.type || 'text'}
                    step={f.step}
                    value={form[f.key] ?? ''}
                    placeholder={f.placeholder || ''}
                    onChange={e =>
                      setForm({
                        ...form,
                        [f.key]:
                          f.type === 'number'
                            ? Number(e.target.value)
                            : e.target.value
                      })
                    }
                  />
                )}
              </label>
            ))}

            {err && <div className="form-error">{err}</div>}

            <div className="modal-actions">
              <button
                type="button"
                className="secondary"
                onClick={() => setOpen(false)}
              >
                Cancel
              </button>
              <button className="primary" disabled={busy}>
                {busy ? 'Saving…' : 'Save record'}
              </button>
            </div>
          </form>
        </div>
      )}
    </>
  );

  return modal;
}

function ApplicationsPage({ rows, companies, refresh }: any) {
  const fields = [
    {
      key: 'companyId',
      label: 'Company',
      type: 'select',
      options: companies.map((c: Company) => ({
        value: c.id,
        label: c.companyName
      })),
      cast: Number
    },
    { key: 'jobRole', label: 'Job role' },
    { key: 'jobLocation', label: 'Job location' }
  ];

  const modal = useCreate(
    v => services.createApplication(v),
    refresh,
    fields
  );

  return (
    <>
      {modal}
      <TablePage
        rows={rows}
        columns={[
          { key: 'companyName', label: 'Company' },
          { key: 'jobRole', label: 'Job role' },
          {
            key: 'jobLocation',
            label: 'Location',
            render: (r: Application) => (
              <span>
                <MapPin size={13} className="inline-icon" />
                {r.jobLocation}
              </span>
            )
          },
          {
            key: 'status',
            label: 'Status',
            render: (r: Application) => (
              <span className="status">{title(r.status)}</span>
            )
          }
        ]}
        onDelete={async (r: Application) => {
          if (
            confirm(
              `Delete ${r.jobRole} at ${r.companyName}? Related records will also be deleted.`
            )
          ) {
            await services.deleteApplication(r.id);
            refresh();
          }
        }}
        actions={(r: Application) => (
          <select
            className="mini-select"
            value={r.status}
            onChange={async e => {
              await services.updateApplicationStatus(
                r.id,
                e.target.value
              );
              refresh();
            }}
          >
            {statusOptions.map(s => (
              <option key={s}>{s}</option>
            ))}
          </select>
        )}
      />
    </>
  );
}

function CompaniesPage({ rows, refresh }: any) {
  const modal = useCreate(
    v => services.createCompany(v.companyName),
    refresh,
    [{ key: 'companyName', label: 'Company name' }]
  );

  return (
    <>
      {modal}
      <TablePage
        rows={rows}
        columns={[
          { key: 'companyName', label: 'Company name' }
        ]}
        onDelete={async (r: Company) => {
          if (confirm(`Delete ${r.companyName}?`)) {
            await services.deleteCompany(r.id);
            refresh();
          }
        }}
      />
    </>
  );
}

function ContactsPage({ rows, companies, refresh }: any) {
  const modal = useCreate(
    v => {
      const { companyId, ...rest } = v;
      return services.createContact(Number(companyId), rest);
    },
    refresh,
    [
      {
        key: 'companyId',
        label: 'Company',
        type: 'select',
        options: companies.map((c: Company) => ({
          value: c.id,
          label: c.companyName
        })),
        cast: Number
      },
      { key: 'name', label: 'Contact name' },
      { key: 'email', label: 'Email', type: 'email', required: false },
      { key: 'phone', label: 'Phone', required: false },
      { key: 'designation', label: 'Designation', required: false }
    ]
  );

  return (
    <>
      {modal}
      <TablePage
        rows={rows}
        columns={[
          { key: 'name', label: 'Name' },
          { key: 'companyName', label: 'Company' },
          { key: 'designation', label: 'Designation' },
          { key: 'email', label: 'Email' },
          { key: 'phone', label: 'Phone' }
        ]}
        onDelete={async (r: HRContact) => {
          if (confirm(`Delete ${r.name}?`)) {
            await services.deleteContact(r.id);
            refresh();
          }
        }}
      />
    </>
  );
}

function InterviewsPage({ rows, apps, refresh }: any) {
  const modal = useCreate(
    v => {
      const { applicationId, ...rest } = v;

      return services.createInterview(Number(applicationId), {
        ...rest,
        roundNumber: Number(rest.roundNumber),
        scheduledAt: rest.scheduledAt || null
      });
    },
    refresh,
    [
      {
        key: 'applicationId',
        label: 'Application',
        type: 'select',
        options: apps.map((a: Application) => ({
          value: a.id,
          label: `${a.companyName} — ${a.jobRole}`
        })),
        cast: Number
      },
      { key: 'roundNumber', label: 'Round number', type: 'number' },
      { key: 'roundName', label: 'Round name' },
      {
        key: 'interviewType',
        label: 'Interview type',
        type: 'select',
        options: [
          'TECHNICAL',
          'HR',
          'MANAGERIAL',
          'CODING',
          'SYSTEM_DESIGN',
          'BEHAVIORAL',
          'CULTURAL_FIT',
          'OTHER'
        ]
      },
      {
        key: 'scheduledAt',
        label: 'Scheduled at',
        type: 'datetime-local',
        required: false
      },
      { key: 'notes', label: 'Notes', required: false }
    ]
  );

  return (
    <>
      {modal}
      <TablePage
        rows={rows}
        columns={[
          { key: 'companyName', label: 'Company' },
          { key: 'jobRole', label: 'Role' },
          { key: 'roundName', label: 'Round' },
          {
            key: 'interviewType',
            label: 'Type',
            render: (r: Interview) => title(r.interviewType)
          },
          {
            key: 'scheduledAt',
            label: 'Scheduled',
            render: (r: Interview) =>
              r.scheduledAt
                ? new Date(r.scheduledAt).toLocaleString()
                : 'Not scheduled'
          },
          {
            key: 'status',
            label: 'Status',
            render: (r: Interview) => (
              <span className="status">{title(r.status)}</span>
            )
          },
          {
            key: 'result',
            label: 'Result',
            render: (r: Interview) => (
              <span className="status">{title(r.result)}</span>
            )
          }
        ]}
        onDelete={async (r: Interview) => {
          if (confirm('Delete this interview round?')) {
            await services.deleteInterview(r.id);
            refresh();
          }
        }}
        actions={(r: Interview) => (
          <select
            className="mini-select"
            value={r.result}
            onChange={async e => {
              await services.setInterviewResult(r.id, e.target.value);
              refresh();
            }}
          >
            <option>PENDING</option>
            <option>PASSED</option>
            <option>FAILED</option>
            <option>ON_HOLD</option>
          </select>
        )}
      />
    </>
  );
}

function OffersPage({ rows, apps, refresh }: any) {
  const modal = useCreate(
    v => {
      const { applicationId, ...rest } = v;

      return services.createOffer(Number(applicationId), {
        ...rest,
        offeredCtc: Number(rest.offeredCtc),
        fixedCtc: rest.fixedCtc ? Number(rest.fixedCtc) : null,
        variableCtc: rest.variableCtc
          ? Number(rest.variableCtc)
          : null,
        joiningBonus: rest.joiningBonus
          ? Number(rest.joiningBonus)
          : null
      });
    },
    refresh,
    [
      {
        key: 'applicationId',
        label: 'Application',
        type: 'select',
        options: apps.map((a: Application) => ({
          value: a.id,
          label: `${a.companyName} — ${a.jobRole}`
        })),
        cast: Number
      },
      {
        key: 'offeredCtc',
        label: 'Offered CTC (INR LPA)',
        type: 'number',
        step: '0.01'
      },
      {
        key: 'fixedCtc',
        label: 'Fixed CTC',
        type: 'number',
        step: '0.01',
        required: false
      },
      {
        key: 'variableCtc',
        label: 'Variable CTC',
        type: 'number',
        step: '0.01',
        required: false
      },
      {
        key: 'joiningBonus',
        label: 'Joining bonus',
        type: 'number',
        step: '0.01',
        required: false
      },
      { key: 'offerDate', label: 'Offer date', type: 'date' },
      {
        key: 'validityDate',
        label: 'Validity date',
        type: 'date',
        required: false
      },
      { key: 'notes', label: 'Notes', required: false }
    ]
  );

  return (
    <>
      {modal}
      <TablePage
        rows={rows}
        columns={[
          { key: 'companyName', label: 'Company' },
          { key: 'jobRole', label: 'Role' },
          { key: 'revisionNumber', label: 'Revision' },
          {
            key: 'offeredCtc',
            label: 'Offered CTC',
            render: (r: Offer) => money(r.offeredCtc)
          },
          {
            key: 'offerDate',
            label: 'Offer date',
            render: (r: Offer) => date(r.offerDate)
          },
          {
            key: 'status',
            label: 'Status',
            render: (r: Offer) => (
              <span className="status">{title(r.status)}</span>
            )
          }
        ]}
        onDelete={async (r: Offer) => {
          if (confirm('Delete this offer?')) {
            await services.deleteOffer(r.id);
            refresh();
          }
        }}
        actions={(r: Offer) => (
          <select
            className="mini-select"
            value={r.status}
            onChange={async e => {
              await services.setOfferStatus(r.id, e.target.value);
              refresh();
            }}
          >
            {['PENDING', 'ACCEPTED', 'REJECTED'].map(s => (
              <option key={s}>{s}</option>
            ))}
          </select>
        )}
      />
    </>
  );
}

function FollowupsPage({ rows, apps, refresh }: any) {
  const modal = useCreate(
    v => {
      const { applicationId, ...rest } = v;

      return services.createFollowup(Number(applicationId), {
        ...rest,
        hrContactId: rest.hrContactId
          ? Number(rest.hrContactId)
          : null
      });
    },
    refresh,
    [
      {
        key: 'applicationId',
        label: 'Application',
        type: 'select',
        options: apps.map((a: Application) => ({
          value: a.id,
          label: `${a.companyName} — ${a.jobRole}`
        })),
        cast: Number
      },
      { key: 'followUpDate', label: 'Follow-up date', type: 'date' },
      {
        key: 'followUpType',
        label: 'Type',
        type: 'select',
        options: ['CALL', 'EMAIL', 'LINKEDIN', 'OTHER']
      },
      { key: 'description', label: 'Description' },
      { key: 'notes', label: 'Notes', required: false }
    ]
  );

  return (
    <>
      {modal}
      <TablePage
        rows={rows}
        columns={[
          { key: 'companyName', label: 'Company' },
          { key: 'jobRole', label: 'Role' },
          {
            key: 'followUpDate',
            label: 'Date',
            render: (r: FollowUp) => date(r.followUpDate)
          },
          {
            key: 'followUpType',
            label: 'Channel',
            render: (r: FollowUp) => title(r.followUpType)
          },
          { key: 'description', label: 'Description' },
          {
            key: 'status',
            label: 'Status',
            render: (r: FollowUp) => (
              <span className="status">{title(r.status)}</span>
            )
          }
        ]}
        onDelete={async (r: FollowUp) => {
          if (confirm('Delete this follow-up?')) {
            await services.deleteFollowup(r.id);
            refresh();
          }
        }}
        actions={(r: FollowUp) => (
          <select
            className="mini-select"
            value={r.status}
            onChange={async e => {
              await services.setFollowupStatus(r.id, e.target.value);
              refresh();
            }}
          >
            <option>PENDING</option>
            <option>COMPLETED</option>
            <option>CANCELLED</option>
          </select>
        )}
      />
    </>
  );
}

function ActivityPage({ rows }: any) {
  return (
    <TablePage
      rows={rows}
      columns={[
        {
          key: 'activityDate',
          label: 'Date',
          render: (r: Activity) => date(r.activityDate)
        },
        { key: 'companyName', label: 'Company' },
        { key: 'jobRole', label: 'Role' },
        {
          key: 'activityType',
          label: 'Type',
          render: (r: Activity) => title(r.activityType)
        },
        { key: 'title', label: 'Activity' },
        { key: 'description', label: 'Details' }
      ]}
    />
  );
}

export default App;
